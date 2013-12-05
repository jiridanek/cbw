/*
 * This is a Java port of the original Python module described in
 *
 *      Xin Zhou and Zhen Su, tCal: transcriptional probability calculator
 *      using thermodynamic model, Bioinformatics 2008 24(22): 2639-2640.
 * 
 * Original author: Xin Zhou (xzhou82@gmail.com)
 * Get the paper and original code at http://bioinformatics.cau.edu.cn/tCal/
 *
 * To use, modify and redistribute this code, conditions of both the original
 * licence and the new one must be fullfiled.
 */

/* Original code licence */

//# for thermodynamics model of gene transcription
//#
//# Author: Xin Zhou (xzhou82@gmail.com)
//# Please address all problems to this person
//#
//# Users are free to modify and redistribute this module, as long as the original authorship is credited.
//#
//#
//#######################################
//#
//# Note that all energy values are in units of 'k_{B}T'
//# 
//#
//# created: 2008 05 21
//#
//# modified: 2008 06 05
//# modified: 2008 06 09
//#	String formula export enabled.
//# modified: 2008 08 12
//#	Enable user to specify forbidden binding events,
//#	so that they escape calculation, combine method got changed as well.
//#	Add replication check for addBindingSite method.

/* New code licence */

/*
 * (C) Copyright 2013 Computational Biology Workspace (http://lrss.fri.uni-lj.si/bio/cbw.html)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Contributors:
 *     Jirka Daněk <dnk@mail.muni.cz>
 */
package fri.cbw.ThermodynamicSimulationEngine;

import static java.lang.Math.exp;
import static java.lang.Math.pow;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

/// FIXME: make sure that some ints here dont break floating point math later in code
public class TranscriptionUnit {

    /** number of effective RNAP molecules that can transcribe this unit */
    private final int rnapNumber;

    /** difference of DNA binding energy of RNAP to the specific site of this transcription unit */
    private final float rnapDnaEnergy;

    /** set of names (SORTED!) */
    private final SortedSet<String> regulatorNames;

    private class RegConfig {
        /** number of molecules */
        public int number;
        /** interaction energy with DNA */
        public float dnaEnergy;
        /** interaction energy with RNAP */
        public float rnapEnergy;
    }
    /**holds individual configurations for each regulator
     key: regulator name*/
    private final Map<String, RegConfig> regConfig;

    /// TODO: this can be moved inside a method
    /**key: regulator name value: a list of binding site IDs */
    Map<String, List<Integer>> occupVector;


    private class SiteConfig {
        /** binding regulator name, must exists in self.regulatorNames */
        String reg;
        /** binding energy */
        float energy;
    }
    /** holds binding sites key: site ID */
    Map<Integer, SiteConfig> bindingSites;

    /// FIXME: make this a get on length
    /** total number of binding sites */
    int bsNum;

    class EnergyConfig {
        /** inter energy of the group */
        float gEnergy;
        /** inter energy of the group with RNAP */
        float gpEnergy;
    }
    /**
     * holds interaction energy between a group of regulators or binding sites
     * key: tuple of binding site ids (SORTED!)
     */
    Map<SortedSet<Integer>, EnergyConfig> interEnergy;

    /**
     * holds forbidden binding events
     * key: set of binding site ids (SORTED!)
     */
    SortedSet<SortedSet<Integer>> forbiddenEvents;

    /// FIXME: convert to getter
    /**
     * indicates if forbidden events exists False by default If forbidden event
     * is successfully added, it is changed to True
     */
    boolean hasForbidden;

    /** */
    float genomeBg;

    /// FIXME: convert to getter
    int regulatorSpeciesNumber;

    /**
     * Defines (lexicographical, sort of) ordering on SortedSets.
     * FIXME: Do we also need HashCode?
     * @param <T> Comparable type
     */
    static class SortedSetComparator<T extends Comparable<T>> implements Comparator<SortedSet<T>> {
        @Override
        public int compare(SortedSet<T> o1, SortedSet<T> o2) {
            /// FIXME: drop this, keep only the else branch
            boolean o1geto2 = o1.containsAll(o2);
            boolean o2geto1 = o2.containsAll(o1);
            if (o1geto2 && o2geto1) { // o1 == o2
                return 0;
            } else if (o1geto2) { // o1 > o2
                return 1;
            } else if (o2geto1) { // o2 > o1
                return -1;
            } else {
                // go through in order, compare values
                int c;
                Iterator<T> i1 = o1.iterator();
                Iterator<T> i2 = o2.iterator();
                while(i1.hasNext() && i2.hasNext()) {
                    T a = i1.next();
                    T b = i2.next();
                    c = a.compareTo(b);
                    if (c != 0) {                        
                        return c;
                    }
                }
                // find which was shorter
                boolean ahas = i1.hasNext();
                boolean bhas = i2.hasNext();
                if (!ahas && !bhas) {
                    return 0;
                } else if (!ahas) {
                    return -1;
                } else {
                    return 1;
                }
            }
        }
   }
    
    

    /**
     * Defines a transcription unit.
     *
     * @param rnapNumber number of RNAP molecules
     * @param rnapDnaEnergy RNAP binding energy
     * @param genomeBg Number of non-specific sites, genome background
     */
    public TranscriptionUnit(int rnapNumber, float rnapDnaEnergy, float genomeBg) {
        this.rnapNumber = rnapNumber;
        this.rnapDnaEnergy = rnapDnaEnergy;
        this.genomeBg = genomeBg;
        this.regulatorSpeciesNumber = 0;
        this.regulatorNames = new TreeSet<String>();
        this.regConfig = new TreeMap<String, RegConfig>();
        this.occupVector = new TreeMap<String, List<Integer>>();
        this.bindingSites = new TreeMap<Integer, SiteConfig>();
        this.bsNum = 0;
        this.interEnergy = new TreeMap<SortedSet<Integer>, EnergyConfig>(new SortedSetComparator<Integer>());
        this.forbiddenEvents = new TreeSet<SortedSet<Integer>>(new SortedSetComparator<Integer>());
        this.hasForbidden = false;
    }

    /**
     * Adds one regulator.
     *
     * Notice: If the added regulator has been added before,
     * tCal will not complain but will accept new
     *
     * @param name number (obsolete)
     * @param rp_energy RNAP interaction energy
     */
    public void addRegulator(String name, float rp_energy) {
        RegConfig config = new RegConfig();
        config.number = 0;
        config.rnapEnergy = rp_energy;
        // if key exists, put() replaces with new value
        // and add() leaves unchanged. This is OK here
        this.regConfig.put(name, config);
        this.regulatorNames.add(name);
        this.occupVector.put(name, new ArrayList<Integer>());
    }

    /**
     * Sets the number of molecules of a regulator.
     *
     * @param name name of the regulator
     * @param number number of molecules
     */
    public void setRegMolNumber(String name, int number) {
        this.regConfig.get(name).number = number;
    }

    /**
     * FIXME: in Python the id could be both integer or string
     *
     * @param id binding site id, will serve as key
     * @param reg name of binding regulator
     * @param dEnergy binding energy at this site, minus background binding
     * energy
     */
    public void addBindingSite(int id, String reg, float dEnergy) {
        SiteConfig siteConfig = new SiteConfig();
        siteConfig.reg = reg;
        siteConfig.energy = dEnergy;
        if (!this.regulatorNames.contains(reg)) {
            throw new Error("UnknownRegulatorError");
        }
        if (this.bindingSites.containsKey(id)) {
            return; //FIXME: silent failure?
        }
        this.bindingSites.put(id, siteConfig);
        this.bsNum += 1;
    }

    /**
     * Specifies interaction energy of a group
     *
     * @param names binding site IDs
     * @param g_energy group interaction energy
     * @param gp_energy inter energy between the group and RNAP
     */
    public void addInteractGroup(SortedSet<Integer> names, float g_energy, float gp_energy) {
        if (names.size() < 2) {
            return;
        }
        for (int siteID : names) {
            if (!this.bindingSites.containsKey(siteID)) {
                throw new Error("UnknownBindingsiteError");
            }
        }
        EnergyConfig energyConfig = new EnergyConfig();
        energyConfig.gEnergy = g_energy;
        energyConfig.gpEnergy = gp_energy;
        this.interEnergy.put(names, energyConfig);
    }

    /**
     * Adds one forbidden binding event.
     * This prevents it from joining computation.
     *
     * @param names binding site id's, indicating a binding event
     */
    public void addForbiddenEvent(SortedSet<Integer> names) {
        if (names.size() < 2) {
            return;
        }
        for (int siteID : names) {
            if (!this.bindingSites.containsKey(siteID)) {
                throw new Error("UnknownBindingsiteError");
            }
        }
        this.forbiddenEvents.add(names);
        this.hasForbidden = true;
    }

    /// FIXME: return value
    /**
     * Combinatory function. Note that the forbidden binding events are excluded.
     *
     * @param number number of binding sites to draw from the pool e.g. to draw
     * 2 sites from a pool of 4, there are 6 combinations
     * @return set of combinations of binding sites, each combination is a set itself
     */
    public SortedSet<SortedSet<Integer>> combine(int number) {
        Set<Integer> bsIDs = this.bindingSites.keySet();
        SortedSet<SortedSet<Integer>> result = new TreeSet<SortedSet<Integer>>(new SortedSetComparator<Integer>());

        /// FIXME: does it make sense to implement "shortcuts"?
        SortedSet<Integer> set = new TreeSet<Integer>();

        if (number == 1) {
            for (int id : bsIDs) {
                set.add(id);
                result.add(set);
            }
            return result;
        }

        if (number == this.bsNum) {
            set = new TreeSet<Integer>(this.bindingSites.keySet());
            /// FIXME: superset question again
            //if (this.hasForbidden) {
                if (!this.forbiddenEvents.contains(set)) {
                    result.add(set);
                }
            //} else {
                //result.add(set);
            //}
            return result;
        }

        /// in python he should've used itertools.combinations(iterable, r)
        /// but maybe it was not available in 2008
        /// TODO: dont enumerate explicitly, return iterator instead?
        List<Integer> keyList = new ArrayList<Integer>(this.bindingSites.keySet());
        int[] indices = new int[number];
        for (int i = 0; i < number; i++) {
            indices[i] = i;
            set.add(keyList.get(i));
        }
        // first combination
        if (!this.forbiddenEvents.contains(set)) {
            result.add(set);
        }
        // translation of Python's itertools.combinations(iterable, r)
        int n = this.bindingSites.size();
        do {
            int i;
            for (i = number - 1; i != -1; i--) {
                // find first index that is not in the final position
                if (indices[i] != i + n - number) {
                    break;
                }
            }
            if (i != -1) {
                indices[i] += 1;

                for (int j = i + 1; j < number; j++) {
                    indices[j] = indices[j - 1] + 1;
                }

                set = new TreeSet<Integer>();
                for (int k = 0; k < number; k++) {
                    set.add(keyList.get(indices[k]));
                }
                /// FIXME: what if `forbiddenEvents` contains a superset of `set`
                if (!this.forbiddenEvents.contains(set)) {
                    result.add(set);
                }
            } else {
                return result;
            }
        } while (true);
    }

    /**
     * Compute basal RNAP specific-binding probability.
     *
     * @return basal RNAP specific-binding probability
     */
    public double baseBindingProb() {
        return 1 / (1 + this.genomeBg * exp(this.rnapDnaEnergy) / this.rnapNumber);
    }

    /**
     * Calculate RNAP binding probability.
     *
     * @return RNAP binding probability
     */
    public double bindingProb() {
        //throw new UnsupportedOperationException("Not supported yet.");

        double divisor = 1.0;
        double denominator = 1.0;
        // step1: make sum over binding sites in which only 1 regulator binds
        for (int site : this.bindingSites.keySet()) {
            String reg = this.bindingSites.get(site).reg; // regulator name
            float dE = this.bindingSites.get(site).energy; // diff binding energy
            divisor += this.regConfig.get(reg).number * exp(-dE - this.regConfig.get(reg).rnapEnergy) / this.genomeBg;
            denominator += this.regConfig.get(reg).number * exp(-dE) / this.genomeBg;
        }
        // step2: sum over all possible combinatorial states
        for (int i = 2; i < this.bsNum + 1; i++) {
            SortedSet<SortedSet<Integer>> combs = this.combine(i);
            for (SortedSet<Integer> com : combs) {       
                // derive occupancy vector
                Map<String, List<Integer>> occup = new TreeMap<String, List<Integer>>();
                for (Map.Entry<String, List<Integer>> entry : this.occupVector.entrySet()) { // deep copy
                    occup.put(entry.getKey(), new ArrayList<Integer>(entry.getValue()));
                }
                
                for (int bsite : com) { // for each binding site
                    occup.get(this.bindingSites.get(bsite).reg).add(bsite);
                }
                double divisorProduct = 1.0; // product for divisor
                double divisorPower = 0.0; // sum of powers for divisor
                double denomiProduct = 1.0; // product for denominator
                double denomiPower = 0.0; // sum of powers for denominator
                for (String reg: occup.keySet()) {
                    if (occup.get(reg).isEmpty()) {
                        //this regulator does not have specific binding
                        continue;
                    }
                    double tmpVar = pow(this.regConfig.get(reg).number / this.genomeBg, occup.get(reg).size());
                    divisorProduct *= tmpVar;
                    denomiProduct *= tmpVar;
                    for (int bsite : occup.get(reg)) { // for each binding site
                        double dE = this.bindingSites.get(bsite).energy;
                        divisorPower += dE + this.regConfig.get(reg).rnapEnergy;
                        denomiPower += dE;
                    }
                }
                /// FIXME: the next comment is a lie
                // check if any interaction group is subset of comSet
                for (Map.Entry<SortedSet<Integer>, EnergyConfig> group : this.interEnergy.entrySet()) {
                    //groupSet = set(group) # convert tuple to set
                    SortedSet<Integer> keys;

                    if (com.containsAll(group.getKey())) {
                        divisorPower += this.interEnergy.get(group.getKey()).gEnergy + this.interEnergy.get(group.getKey()).gpEnergy;
                        denomiPower += this.interEnergy.get(group.getKey()).gEnergy;
                    }
                }
                divisor += divisorProduct * exp(-divisorPower);
                denominator += denomiProduct * exp(-denomiPower);
            }
        }
        double Fterm = divisor / denominator;
        return 1.0 / (1.0 + this.genomeBg * exp(this.rnapDnaEnergy) / (this.rnapNumber * Fterm));
    }

    /**
     * Return the string formula to calculate RNAP binding probability. This
     * could be used to construct RateRule instance in a SBML model.
     *
     * @return formula to calculate RNAP binding probability
     */
    public String bindingProbFormula() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
