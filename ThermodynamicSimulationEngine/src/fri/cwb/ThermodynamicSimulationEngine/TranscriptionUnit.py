# for thermodynamics model of gene transcription
#
# Author: Xin Zhou (xzhou82@gmail.com)
# Please address all problems to this person
#
# Users are free to modify and redistribute this module, as long as the original authorship is credited.
#
#
#######################################
#
# Note that all energy values are in units of 'k_{B}T'
#
#
# created: 2008 05 21
#
# modified: 2008 06 05
# modified: 2008 06 09
#	String formula export enabled.
# modified: 2008 08 12
#	Enable user to specify forbidden binding events,
#	so that they escape calculation, combine method got changed as well.
#	Add replication check for addBindingSite method.




import sys,math,copy

class TranscriptionUnit:
	"""The transcription unit composed by a target gene and its regualtors.

	[ Attributes ]
	rnapNumber
		number of effective RNAP molecules that can transcribe this unit
	rnapDnaEnergy
		float, the difference of DNA binding energy of RNAP to the specific
		site of this transcription unit
	regulatorNames
		list holding names
		NOTE THAT THIS IS SORTED
	regConfig
		dict, to hold individual configurations for each regulator
		including: number of molecule, DNA interaction energy, 
		RNAP interaction energy
		key: regulator name
		val: dict
		    'number' : number of molecules,
			'dnaEnergy' : interaction energy with DNA
			'rnapEnergy' : interaction energy with RNAP
	occupVector
		dict, the initial form of occupancy vector
		a copy of it will be made to compose real occupancy vector
		key: regulator name
		value: [] empty list
	bindingSites
		dict, to hold binding sites
		key: site ID, integer
		val: dict
			reg: binding regulator name, must exists in self.regulatorNames
			energy: binding energy
	bsNum
		integer, total number of binding sites
	interEnergy
		dict, to hold interaction enegy between a group of
			regulators or binding sites
		key: tuple of binding site ids 
			(ascending order, produced by sorted function)
		val: dict
			'gEnergy' : inter energy of the group
			'gpEnergy' : inter energy of the group with RNAP
	forbiddenEvents
		dict, to hold forbidden binding events
		key: tuple to binding site ids, _sorted_
		val: 1
	hasForbidden
		boolean, to indicate if forbidden events exists
		False by default
		If forbidden event is succesfully added, it is changed to True
	"""
	def __init__(self,rnapNum,rnapEn,genomeBg):
		"""Create a transcription unit.

		[ argument ]
		rnapNum
			number of RNAP molecules
		rnapEn
			RNAP binding energy
		genomeBg
			Number of non-specific sites, genome background
		"""
		if(type(rnapNum) != type(1) and type(rnapNum) != type(1.0)):
			raise TypeError,'Number of RNA polymerase molecules must be numeric'
		else:
			self.rnapNumber = rnapNum
		if(type(rnapEn) != type(1.0) and type(rnapEn) != type(1)):
			raise TypeError,'RNA polymerase binding energy must be numeric'
		else:
			self.rnapDnaEnergy = rnapEn
		if(type(genomeBg) != type(1) and type(genomeBg) != type(1.0)):
			raise TypeError,'Number of non-specific binding sites in the genome background must be numeric'
		else:
			self.genomeBg = genomeBg
		self.regulatorSpeciesNumber = 0
		self.regConfig = {}
		self.occupVector = {}
		self.regulatorNames = []
		self.bindingSites = {}
		self.bsNum = 0
		self.interEnergy = {}
		self.hasForbidden = False
		self.forbiddenEvents = {}
	def addRegulator(self,name,rp_energy):
		"""Add one regulator.

		Notice: If the added regulator has been added before,
		tCal will not complain but will accept new 
		[ argument ]
		name
		number (obsolete)
		rp_energy
			RNAP interaction energy
		"""
		if(type(rp_energy) != type(1.0) and type(rp_energy) != type(1)):
			raise TypeError,'RNA polymerase binding energy must be numeric'
		self.regConfig[name] = {
			'number':0,
			'rnapEnergy':rp_energy }
		self.regulatorNames.append(name)
		self.regulatorNames = sorted(self.regulatorNames)
		self.occupVector[name] = []
	def setRegMolNumber(self,name,number):
		"""Change molecule number for one regulator.

		[ argument ]
		name
		number
		"""
		if(name not in self.regulatorNames):
			raise UnknownRegulatorError
		if(type(number) != type(1) and type(number) != type(1.0)):
			raise TypeError,'Number of regulator molecules must be numeric'
		self.regConfig[name]['number'] = number
	def addBindingSite(self,id,reg,dEnergy):
		"""Add one binding site.

		[ argument ]
		id
			integer or string, binding site id, will serve as key
		reg
			name of binding regulator
		dEnergy
			binding energy at this site, minus background binding energy
		"""
		if reg not in self.regulatorNames:
			raise UnknownRegulatorError
		if id in self.bindingSites:
			# do nothing for replicated binding site IDs
			return
		if(type(dEnergy) != type(1.0) and type(dEnergy) != type(1)):
			raise TypeError,'Regulator binding energy must be numeric'
		self.bindingSites[id] = {'reg':reg,'energy':dEnergy}
		self.bsNum += 1
	def addInteractGroup(self,names,g_energy,gp_energy):
		"""Add an interacting regulator group.

		[ argument ]
		names
			a tuple of binding site IDs
		g_energy
			group interaction energy
		gp_energy
			inter energy between the group and RNAP
		"""
		if len(names)<2:
#			number of interacting regulators must be greater than 1
			return
		for siteID in names:
			if siteID not in self.bindingSites:
				raise UnknownBindingsiteError
		if(type(g_energy) != type(1.0) and type(g_energy) != type(1)):
			raise TypeError,'Regulators interaction energy must be numeric'
		if(type(gp_energy) != type(1.0) and type(gp_energy) != type(1)):
			raise TypeError,'Interaction energy between the group of regulators and RNA polymerase must be numeric'
#		convert to sorted tuple
		sname = tuple(sorted(names))
		self.interEnergy[sname] = {'gEnergy':g_energy,'gpEnergy':gp_energy}
	def addForbiddenEvent(self,names):
		"""Add one forbidden binding event.
		This prevents it from joining computation.

		[ argument ]
		names
			a tuple of binding site ids, indicating a binding event
		"""
		if(len(names)<2):
			# number of binding sites must be greater than 1
			return
		for siteID in names:
			if siteID not in self.bindingSites:
				raise UnknownBindingsiteError
		# convert to sorted tuple
		sname = tuple(sorted(names))
		self.forbiddenEvents[sname] = 1
		if(self.hasForbidden == False):
			self.hasForbidden = True

	def combine(self,number):
		"""Combinatory function.
		returns a tuple of combinations of binding sites, each
		combination is a tuple itself.
		Note that the forbidden binding events are excluded.

		[ argument ]
		number
			number of binding sites to draw from the pool
			e.g. to draw 2 sites from a pool of 4, there are 6 combinations
		"""
#		generate a list of binding site IDs
		bsIDs = self.bindingSites.keys()
#		index IDs
		total = range(self.bsNum)
#		hold results
		result = []
#		following are shortcuts
		if number == 1:
			for id in bsIDs:
				result.append((id,))
			return tuple(result)
		if number == self.bsNum:
			ttmp = tuple(bsIDs)
			if self.hasForbidden == True:
				if ttmp in self.forbiddenEvents:
					return ()
				else:
					return (ttmp,)
			else:
				return (ttmp,)
#		add first combination
		result.append(tuple(bsIDs[0:number],))
#		calculate total number of combinations
		allNum = 1
		for i in range(self.bsNum-number+1,self.bsNum+1):
			allNum *= i
		for i in range(1,number+1):
			allNum /= i
#		print 'Total %d' % allNum
		i=2
		j=0
		current = range(0,number) # index list of first combination
		for i in range(2,allNum+1):
			m=number-1
			index = self.bsNum-1
			while current[m]==total[index]:
				m -= 1
				index -= 1
			while True:
				if total[index]==current[m]:
					break
				index -= 1
			index += 1
			current[m]=total[index]
			for j in range(m+1,number):
				index += 1
				current[j] = total[index]
			this=[]
			for ind in current:
				this.append(bsIDs[ind])
			ttmp = tuple(this) # temp tuple
			if self.hasForbidden == True:
				if ttmp not in self.forbiddenEvents:
					result.append(ttmp)
			else:
				result.append(ttmp)
		return tuple(result)
	def baseBindingProb(self):
		"""Compute basal RNAP specific-binding probability.

		"""
		return 1/(1+self.genomeBg*math.exp(self.rnapDnaEnergy)/self.rnapNumber)
	def bindingProb(self):
		"""Calculate RNAP binding probability.

		"""
		divisor = 1.0
		denominator = 1.0

#		step1: make sum over binding sites in which only 1 regulator binds
		for site in self.bindingSites:
			reg = self.bindingSites[site]['reg'] # regulator name
			dE = self.bindingSites[site]['energy'] # diff binding energy
			divisor += self.regConfig[reg]['number']*math.exp(-dE-self.regConfig[reg]['rnapEnergy'])/self.genomeBg
			denominator += self.regConfig[reg]['number']*math.exp(-dE)/self.genomeBg

#		step2: sum over all possible combinatorial states
		for i in range(2,self.bsNum+1):
			combs = self.combine(i)
			for com in combs:
#				derive occupancy vector
				occup = copy.deepcopy(self.occupVector)
#				key: regulator name
#				val: a list of binding site IDs, may be empty
				for bsite in com: # for each binding site
					occup[self.bindingSites[bsite]['reg']].append(bsite)
				divisorProduct = 1.0 # product for divisor
				divisorPower = 0.0 # sum of powers for divisor
				denomiProduct = 1.0 # product for denominator
				denomiPower = 0.0 # sum of powers for denominator
				comSet = set(com) # convert this group to set
				for reg in occup:
					if len(occup[reg]) == 0:
#						this regulator does not have specific binding
						continue
					tmpVar = (self.regConfig[reg]['number']/self.genomeBg)**len(occup[reg])
					divisorProduct *= tmpVar
					denomiProduct *= tmpVar
					for bsite in occup[reg]: # for each binding site
						dE = self.bindingSites[bsite]['energy']
						divisorPower += dE+self.regConfig[reg]['rnapEnergy']
						denomiPower += dE
#				check if any interaction group is subset of comSet
				for group in self.interEnergy:
					groupSet = set(group) # convert tuple to set
					if groupSet.issubset(comSet):
						divisorPower += self.interEnergy[group]['gEnergy'] + self.interEnergy[group]['gpEnergy']
						denomiPower += self.interEnergy[group]['gEnergy']
				divisor += divisorProduct * math.exp(-divisorPower)
				denominator += denomiProduct * math.exp(-denomiPower)
		Fterm = divisor/denominator
		return 1/(1+self.genomeBg*math.exp(self.rnapDnaEnergy)/(self.rnapNumber*Fterm))

	def bindingProbFormula(self):
		"""Return the string formula to calculate RNAP binding probability.
		This could be used to construct RateRule instance in a SBML model.

		"""
		divisor = '1' # now we are strings...
		denominator = '1'
#		step1: make sum over binding sites in which only 1 regulator binds
		for site in self.bindingSites:
			reg = self.bindingSites[site]['reg'] # regulator name
			dE = self.bindingSites[site]['energy'] # diff binding energy
			divisor += '+%s*%f' % (reg, math.exp(-dE-self.regConfig[reg]['rnapEnergy'])/self.genomeBg)
			denominator += '+%s*%f' % (reg, math.exp(-dE)/self.genomeBg)

#		step2: sum over all possible combinatorial states
		for i in range(2,self.bsNum+1):
			combs = self.combine(i)
			for com in combs:
#				derive occupancy vector
				occup = copy.deepcopy(self.occupVector)
#				key: regulator name
#				val: a list of binding site IDs, may be empty
				for bsite in com: # for each binding site
					occup[self.bindingSites[bsite]['reg']].append(bsite)
				divisorProduct = '1' # product for divisor, now a string
				divisorPower = 0.0 # sum of powers for divisor
				denomiProduct = '1' # product for denominator, now a string
				denomiPower = 0.0 # sum of powers for denominator
				comSet = set(com) # convert this group to set
				for reg in occup:
					if len(occup[reg]) == 0:
#						this regulator does not have specific binding
						continue
					tmpVar = '*((%s/%d)^%d)' % (reg,self.genomeBg,len(occup[reg]))
					divisorProduct += tmpVar
					denomiProduct += tmpVar
					for bsite in occup[reg]: # for each binding site
						dE = self.bindingSites[bsite]['energy']
						divisorPower += dE+self.regConfig[reg]['rnapEnergy']
						denomiPower += dE
#				check if any interaction group is subset of comSet
				for group in self.interEnergy:
					groupSet = set(group) # convert tuple to set
					if groupSet.issubset(comSet):
						divisorPower += self.interEnergy[group]['gEnergy'] + self.interEnergy[group]['gpEnergy']
						denomiPower += self.interEnergy[group]['gEnergy']
				divisor += '+%s*%f' % (divisorProduct,math.exp(-divisorPower))
				denominator += '+%s*%f' % (denomiProduct,math.exp(-denomiPower))
		Fstring = '(%s)/(%s)' % (divisor,denominator)
		return '1/(1+%f/(%d*(%s)))' % (self.genomeBg*math.exp(self.rnapDnaEnergy),self.rnapNumber,Fstring)

class UnknownRegulatorError(Exception):
	"""Regulator is unknown, maybe wrong spelling.

	"""
	def __init__(self):
		pass
class UnknownBindingsiteError(Exception):
	"""Binding site is unknown.

	"""
	def __init__(self):
		pass