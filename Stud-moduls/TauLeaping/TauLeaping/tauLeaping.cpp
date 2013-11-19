/* 
 * File:   tauLeaping.cpp
 * Author: Jernej Hartman
 * 
 * Implementation of the (optimized) tau leaping algorithm. 
 * Based on article: http://www.cs.ucsb.edu/~cse/Files/NewTau052.pdf
 * 
 * Created on January 8, 2013, 1:07 PM
 */

#include "tauLeaping.h"

tauLeaping::tauLeaping() {
}

tauLeaping::~tauLeaping() {
    reset();
}
/**
 * Starting point of the simulation - evaluates all the parameters
 * @param N - number of species
 * @param M - number of reactions
 * @param x0 - starting state for each species (size = N)
 * @param vMaxLen - size of second dimension of the 2d state vector
 * @param v - 2d state change vector array (size = MxvMaxLen)
 * @param RS - 2d array indicating specie indexes in each reaction (size = MxvMaxLen)
 * @param k - reaction speeds (size = N)
 * @param simTime - total time of the simulation
 * @param omega - system size 
 * @param recordInterval - record each N iteration
 * @param epsilon - error control parameter
 * @param ssaTreshold - constant value (treshold value to determen when to use SSA method instead of ETL - default 10)
 * @param exactIter - how many iterations of SSA (default 100)
 * @param criticalTreshold - constant Nc (treshold value to determin critical reactions - default 10) 
 */
void tauLeaping::startSimulation(long N, long M, long * x0, short vMaxLen, short ** v, long ** RS, double * k, double simTime, long omega, short recordInterval, double epsilon, short ssaTreshold, long exactIter, short criticalTreshold) 
{
    // reset previous simulation parameters
    this->reset();
    
    // system parameters
    this->numReactions = M;
    this->numSpecies = N;
    this->x = x0;
    this->vLen = vMaxLen;
    
    // simulation parameters
    this->simTime = simTime;
    this->recordInterval = recordInterval;
    
    // tau leaping parameters
    this->systemSize = omega;
    this->epsilon = epsilon;
    this->ssaTreshold = ssaTreshold;
    this->criticalTreshold = criticalTreshold;
    this->exactIterations = exactIter;
    
    // validate the parameters
    this->validateParameters(N,M,v,k,RS);    
    
    run();
}
/**
 * Creates an array of all reactions given the total number of reactions, the state change vector for each reaction, reaction speed for each reaction and species indexes of for each reaction
 * @param M
 * @param v
 * @param k
 * @param RS
 */
void tauLeaping::validateParameters(long N, long M, short ** v, double * k, long ** RS)
{
    unsigned long i = 0;
    short j = 0, pr = 0, re= 0, len = 0;
    reaction r;
    
    species = new specie[N];
    reactions = new reaction[M];
    prop = new double[M];
    
    // for each specie init the specie
    for(i = 0; i < N; i++)
    {
         specie s;
         vector<long> react;
         s.reactions = react;
         species[i] = s;
    }
    
    for (i = 0; i < M; i++)
    {
        pr = re = 0;
        // compute number of products, reactants 
        for (j = 0; j < vLen; j++)
        {
            if(v[i][j] < 0)
            {
                if(std::find(species[RS[i][j]].reactions.begin(),species[RS[i][j]].reactions.end(),i) == species[RS[i][j]].reactions.end())
                {     
                    species[RS[i][j]].reactions.push_back(i);
                }
                re++;
            }
            else if(v[i][j] > 0)
            {
                pr++;
            } else break;
        }
        // cout << "Reaction " << i << " has " << re << " reactants " << pr << " products " << k[i] << " speed " << endl;
        
        r.species = RS[i];
        r.nrProducts = pr;
        r.nrReactants = re;      
        r.k = k[i];
        r.v = v[i];
        
        // add reaction to the array
        reactions[i] = r;
    }
    // print all the species
    for(i = 0; i < N; i++)
    {
       // cout << "Species " << i << " is involved in reactions ";
       for(int j = 0; j < species[i].reactions.size(); j++)
       {
          // cout << " " <<  species[i].reactions[j];
       }
       // cout << endl;
    }
    // for each specie find its highest order
    findHOR();
}
/**
 * Main simulation loop
 * @return 
 */
void tauLeaping::run()
{
    double dt = 0;
    long logCount = 0;   
    long realTime = 0;
    // run the loop until the time runs out
//    results[time] = x;
    printData(x,time);
            
    // TODO: check each N iteration for user interrupt or else there will be no way of stopping the simulation!!
    while(time < simTime)
    {
        // step simulation for each reaction 
        dt = this->stepSimulation();
        // increase the simulation time if not 0
         if(dt == 0) continue;
        time += dt;
        iterations++;
        printData(x,time);
    
        // check if we need to record the result
        logCount++;
        if(logCount >= recordInterval)
        {
            // record the result
       //     results[time] = x;
            // reset the log counter
            logCount = 0;
        }
        actualSimTime += realTime;
    }
    cout << "Simulation done after " << time << "sec and " << iterations << " iterations" << endl;
}
/**
 * Performs a modified (non-negative) tau leaping step
 * @return 
 */
double tauLeaping::stepSimulation()
{
    // TODO: add a boolean if we need to execute ssa 
    double dt = 0;
    // clear the previous (non)critical reactions if not using SSA
    if(ssaIteration >= exactIterations)
    {
        // if using SSA check if we finished the max number of SSA iterations
        useSSA = false;
        ssaIteration = 0;
    }
   // useSSA = true;
    
    // calculate the propensities and propensity sum (for each reaction) and find all the (non) critical reactions 
    if(!propCalc)calculatePropensities(numReactions, reactions, x, prop, propSum);
    // check if all molecules are exhausted
    if(propSum > 0)
    {    
        // check if all species exhausted 
        if(useSSA)
        {
            // cout << "Using SSA" <<endl;
            this->single_step_SSA(dt);
        } else
        {
            // cout << "Using ETL"<<endl;
            this->single_step_ETL(dt);
        } 
    } else
    {
        // cout << "All molecules exhausted. Ending simulation." << endl;
        dt = simTime - time;
    }
    return dt;
}

/**
 * Calculates the propensity function for the given reaction and check if the reaction is critical or not.
 * @param reactions
 */
bool tauLeaping::calculatePropensity(const reaction reaction, const long * curState, double & result)
{
    bool isCritical = false;
    // propensity = c * R1[1] * R2[2] * ..
    long i, idx , R = reaction.nrReactants;
    long n2 = 0, consumed = 0, tmp = 0;
    double prod = 1, L = 0, min = 0;
    result = 1;
    // for each reactants in reaction
    for (i = 0; i < R; i++)
    {
        idx = reaction.species[i];
        // stop processing if there aren't any reactant species
        if(curState[idx] == 0) 
        {
            result = 0;
            break;
        }
        consumed = abs(reaction.v[i]);
        n2+=consumed;
        getBinomialValue(curState[idx],consumed,tmp);
        result *= tmp;
        tmp = 0;
        getFactorialValue(consumed,tmp);
        prod *= tmp;
        L = curState[idx] / abs(reaction.v[i]);
        if(L < min || i == 0) min = L;
    }
    // if there are enough species calculate cj
    if(result != 0)
    {
        double c = reaction.k / pow((double)systemSize,(double)n2) * prod;
        result *= c;
        // check if reaction is critical
        isCritical = floor(L) <= criticalTreshold;
    }
    // cout << "Reaction has L = " << L << endl;
    
    return isCritical;
}
/**
 * Calculate the propensity function for all given reaction with the current state and stores it into result. It also computes the sum of all propensity functions.
 * @param M - number of reactions
 * @param reactions - array of all reactions
 * @param curState - the current system state
 * @param result - a array in which to store the results
 * @param sum - a pointer in which to store the propensity sum (a0)
 * @return a boolean indicating if there are any negative values
 */
bool tauLeaping::calculatePropensities(const long M, const reaction * reactions, const long * curState, double *& result, double & sum)
{
    bool isNegative = false;
    bool isCritical = false;
    double propensity = 0;
    unsigned int i = 0;
    sum = 0;
    if(!useSSA)
    {
        critical.clear();
        nonCritical.clear();
    } 
    // for each reaction calculate its propensity and sum the together
    for ( i = 0; i < M; i++)
    {
        propensity = 0;
        
        isCritical = this->calculatePropensity(reactions[i], curState, propensity);
        if(!useSSA)
        {
            if(isCritical)
            {
                // add it to the list of critical reactions
                critical.push_back(i);
            } else
            {
                // add it to non critical list
                nonCritical.push_back(i);
            }
        }
        // cout << "Reaction " << i << " has propensity " << propensity << " and is critical? " << isCritical << endl;
        sum += propensity;
        if(!isNegative && propensity < 0)
        {
            isNegative = true;
        }
        if(result != NULL) 
            result[i] = propensity;
    }
    return isNegative;
}

/**
 * Simulates a SSA step
 * @return consumed time
 */
void tauLeaping::single_step_SSA(double & tau)
{
    long j = 0;
    tau = 0;
    // calculate tau
    getExpRandom(1/propSum,tau);
    // check if curTime + tau > simTime (so we don't simulate too much)
    if(time+tau > simTime) tau = simTime - time;
    // calculate the reaction index
    getPointProbValue(this->prop,this->numReactions,this->propSum,j);  
    // cout << " SSA calculated tau " << tau << " and j " << j << endl;
    // update the state vector for reaction j
    updateState(this->reactions[j],1,x);
    
    propCalc = false;
    ssaIteration++;
}
/**
 * Update the state vector for the given reaction
 * @param R - reaction 
 * @param k - number of reaction firings
 * @param result - pointer in which to store the result
 */
void tauLeaping::updateState(const reaction R, const double k, long *& result)
{
    // update all reactant and product species 
    unsigned short i = 0, len = R.nrProducts+R.nrReactants;
    for (i = 0; i < len; i++)
    {
        result[R.species[i]] += k * R.v[i];
    }
}

/**
 * Simulates a explicit Poisson tau leap step.
 * @return returns the time consumed by the step.
 */
void tauLeaping::single_step_ETL(double & dt)
{
   // // cout << "Steping etl.." <<endl;
    double tau = 0, tau1 = 0, tau2 = 0;
    // check if there are any non critical reactions (if yes skip next step)
    if(nonCritical.size() == 0)
    {
        // cout << "No noncritical reactions.." << endl;
        tau1 = 10e6;
    } else
    {
        // find tau'
        getTau(tau1);
    }
    // cout <<" tau1 = " << tau1 << " prop sum is " << propSum << " treshold " << ssaTreshold/propSum << endl; 
    // check if tau' smaller then a0 * ssaTreshold 
    if(tau1 < ((double)ssaTreshold/propSum))
    {
        // cout << " doing SSA " << endl;
        // if smaller resort to exact SSA and execute exactIterations steps of SSA 
        useSSA = true;
        this->single_step_SSA(dt);
    } else
    {
        // calculate sum of propensity functions of critical reactions = a0c
        double a0c = 0;
        unsigned long i = 0, len = critical.size(),idx = 0;
        for(i = 0; i < len; i++)
        {
            idx = critical[i];
            a0c += prop[idx];
            // cout << " Critical reaction " << i << " has " << prop[idx] << endl;
        }
        // cout << " Got critical sum " << a0c <<" critical reaction count is " << critical.size()<< endl;
        // calculate tau''
        if(a0c > 0)
        {    
           getExpRandom(1/a0c,tau2);
        } else tau2 = 10e6;
        // cout << " tau 2 = " << tau2 << endl;
        bool tauTooBig = true;
        do
        {
            // make backup of current state, propensities and critical/noncritical vectors
            long * prevState = new long[numSpecies];
            double * tmpProp = new double[numReactions];
            vector<long> prevCritical(critical);
            vector<long> prevNonCritical(nonCritical);
            // copy current state
            memcpy(prevState,x,sizeof(prevState));
            // copy propensities
            memcpy(tmpProp,prop,sizeof(tmpProp));
            
            // if tau' < tau'' tau = tau'
            if(tau1 < tau2) 
            {
                tau = tau1;
            }
             // else if tau'' <= tau' 
            else
            {
               tau = tau2;
               // generate jc 
               long jc = 0;
               len = critical.size();
               double * critProp = new double[len];
               for(i = 0; i < len; i++)
               {
                   idx = critical[i];
                   critProp[i] = prop[idx];
               }
               getPointProbValue(critProp,len,a0c,jc);
               // cout << " Updating critical reaction " << jc;
               updateState(reactions[jc],1,x);
               delete[] critProp;
            }
             // check if curTime + tau > simTime (so we don't simulate too much)
            if(time+tau > simTime)
            {
                tau = simTime - time;
                // cout << " tau overflow.. " << tau << endl;
            }
            // cout << "Tau is " << tau << endl;
            long k = 0;
            len = nonCritical.size();
            // update state for all non critical reactions
            for(i = 0; i < len; i++)
            {
                idx = nonCritical[i];
                 // get poission value with mean aj * tau
                getPoissonValue(prop[idx]*tau, k);
                // cout << "Updating non critical " << idx << " poisson " << k << " prop " << prop[idx] << tau* prop[idx] << endl;
                updateState(reactions[idx],k,x);
            }
            
            // check if all propenseties are positive 
            // NOTE: if tau isnt too big we can skip the propensity calculation in the next step as it is already computed, but be sure you dont override the current propensities, its sum, critical and non critical reactions
            tauTooBig = this->calculatePropensities(numReactions, reactions, x, prop, propSum);
            propCalc = true;
            if(tauTooBig)
            {
                // cout << " Tau too big.. "<< endl;
                 // if not tau' = tau' / 2 and goto step 6.
                 tau1 /= 2;  
                 // fall back to previous state
                 memcpy(x,prevState,sizeof(x));
                 memcpy(prop,tmpProp,sizeof(prop));
                 critical.swap(prevCritical);
                 nonCritical.swap(prevNonCritical);
            }
            // cout << " We have " << critical.size() << " c and " << nonCritical.size() << " non c" << endl;
            delete[] prevState;
            delete[] tmpProp;
            prevCritical.clear();
            prevNonCritical.clear();
 
       } while(tauTooBig);
       dt = tau;
    }
}
/**
 * Calculates tau prime.
 * @param tau - pointer in which to store the result
 */
void tauLeaping::getTau(double & tau)
{
    // since we need to iterate through all reactants and calculate a sum of propensity * vij for each non critical reaction 
    // we can simplify this by iterating through only non critical reactants
    // since we dont have precomputed which reactant is in which non critical reaction we can first create 2 hashmaps
    // for mu an sigma and compute the values as we go which will result in a much faster execution
    unsigned long i = 0, len = nonCritical.size(), key;
    unsigned short j = 0, nrReact = 0;
    long idx, idx2;
    double num = 0, bound1 = 0, bound2 = 0, tmp = 0,numVal = 0;
    HashMap mu;
    HashMap sigma;

    // calculate g vector 
    updateG();
    
    // for each non critical reaction
    for(i = 0; i < len; i++)
    {
        idx2 = nonCritical[i];
        nrReact = reactions[idx2].nrReactants;
        // for each reactant in the non critical reaction
        for (j = 0; j < nrReact; j++)
        {
            idx = reactions[idx2].species[j];
          //  // cout << "Non critical reaction " << idx2 << " species " << idx << endl;
            // calculate mu(x) and sigma(x) for the reactant and add it to the hashmaps
            if(mu.find(idx) == mu.end())
            {
                mu[idx] = 0;
                sigma[idx] = 0;
            }
            mu[idx] += reactions[idx2].v[j] * prop[idx2];
            sigma[idx] += reactions[idx2].v[j] * reactions[idx2].v[j] * prop[idx2];
        }
    }
    i = 0;
    // iterate through the hashmaps and find min tau
    for (HashMap::const_iterator it = mu.begin(); it != mu.end(); ++it) 
    {
        key = (*it).first;
        if(species[key].g == 0 ) continue;
        numVal = 0;
        if(x[key] > 0) numVal = (epsilon * x[key]) / species[key].g;
        num = max(numVal,(double)1);
        bound1 = num / fabs((*it).second);
        bound2 = (num*num) / sigma[key];
        // cout << "Key " << key <<" has value " << (*it).second  << " bound1 " << bound1 << " bound2 " << bound2 <<  endl;
        tmp = min(bound1,bound2);
        if(i == 0)tau = tmp;
        else tau = min(tmp,tau);
        i++;
    } 
}

void tauLeaping::printData(long * x, long dt)
{
     cout << "time: " << dt << " ";
     if(iterations > 0)
        if(useSSA) cout << "[SSA]";
        else cout << "[ETL]";
         
     cout << " x[";
    
    for(int i = 0; i < numSpecies; i++)
    {
        if(i > 0) cout << " ";
        cout << x[i] ;
    }
    cout << "] " <<endl;
}
/**
 * Finds the highest order (HOR) of all species. HOR is defined as the highest order of reaction in which Si appears as a reactant. 
 * This method find the HOR for each specie, the index of the HOR reaction and the index in state change vector of the HOR reaction. 
 * @return 
 */
void tauLeaping::findHOR()
{
    // we need to optimize the search of HOR
    // each species should have a list of all the reactions in which it appears as a reactant
    unsigned long i = 0, j = 0, reactIdx = 0;
    unsigned short h = 0,  numReact = 0,  tmpV = 0, nrReactants = 0, k = 0;
    short maxV = -1,horVIdx = 0;
    long horIdx = -1;
    bool dynamic;
    
    // for each species 
    for(i = 0; i < numSpecies; i++)
    {
        dynamic = false;
        // find the highest order of reaction in which species Si appears as a reactant and the reaction index
        // also add a optimization flag if the species is dynamic (if more than 1 molecule of this species in reaction as a reactant)
        h = 0; maxV = -1; horVIdx = -1; horIdx = -1;
                
        numReact = species[i].reactions.size();
        // for each reaction in which species i appears as a reactant
        for (j = 0; j < numReact; j++)
        {
            reactIdx = species[i].reactions[j];
            // find the max HOR(i) and the index of the HOR reaction (if two have same values use the one with bigger number of same molecules)
            if(j == 0 || reactions[reactIdx].nrReactants > h || (reactions[reactIdx].nrReactants == h && h > 1))
            {
                nrReactants = reactions[reactIdx].nrReactants;
                // find the index of this species in the reaction and the number of same molecules.
                for(k = 0; k < nrReactants; k++)
                {
                    if(i == reactions[reactIdx].species[k])
                    {
                        tmpV = abs(reactions[reactIdx].v[k]);
                        break;
                    }
                }
                if(maxV == -1 || tmpV > maxV)
                {
                    h = reactions[reactIdx].nrReactants;
                    horIdx = reactIdx;
                    horVIdx = k;
                    maxV = tmpV;
                    dynamic = tmpV > 1;
                }
            }
        }
       // // cout << "Species " << i << " has HOR " << h << " in reaction " << horIdx << " in vector v[idx] " << horVIdx <<  " reaction has " << numReact << " reactants " << endl; 
        species[i].hor = h;
        species[i].horRIndex = horIdx;
        species[i].horRVIndex = horVIdx;
        species[i].isDynamic = dynamic;
        species[i].g = h;
    }
}
/**
 * Updates the g vector for each species 
 * @return 
 */
void tauLeaping::updateG()
{
 //  // cout << " updating g" << endl;
    unsigned long i = 0;
    unsigned short g = 0;
    unsigned nrOfSameMolecules = 0;
    // for each species 
    for(i = 0; i < numSpecies; i++)
    {
        // is the species dynamic? if no skip it since the g[i] is a constant
    //    // cout << "Species " << i << " has g vector " << species[i].g << " and is " << species[i].isDynamic << endl;
        if(!species[i].isDynamic) continue;
        
        // calculate g (only for dynamic species)
        g = species[i].hor;
        if(g == 2)
        {
            nrOfSameMolecules = abs(reactions[species[i].horRIndex].v[species[i].horRVIndex]);
            if(nrOfSameMolecules == 2)
            {
                g = 2 + (1 / (x[i]-1));
            }
        } else if(g == 3)
        {
            nrOfSameMolecules = abs(reactions[species[i].horRIndex].v[species[i].horRVIndex]);
            if(nrOfSameMolecules == 2)
            {
                g = (2 + (1 / (x[i]-1))) * 1.5f;
            } else if(nrOfSameMolecules == 3)
            {
                g = 3 + (1 / (x[i]-1)) + (2/ (x[i]-2));
            }
        }
        species[i].g = g;
    }
}
/**
 * Resets all the simulation parameters and results of previous simulation.
 * @return 
 */
void tauLeaping::reset()
{
    // clear all the data
    if(x != NULL)
    {
        delete[] x;
        delete[] prop;
        delete[] reactions;
        delete[] species;
    }
    critical.clear();
    nonCritical.clear();
    propCalc = false;
    
    // reset the parameters
    this->numReactions = 0;
    this->numSpecies = 0;
    this->vLen = 0;
    
    this->iterations = 0;
    this->time = 0;
    this->actualSimTime = 0;
    this->recordInterval = 1;
    this->useSSA = false;
    this->ssaIteration = 0;
    this->systemSize = 0;
            
    // tau leaping parameters
    this->epsilon = 0.03;
    this->ssaTreshold = 10;
    this->criticalTreshold = 10;
    this->exactIterations = 100;
 
}
// TODO: save results into something?
