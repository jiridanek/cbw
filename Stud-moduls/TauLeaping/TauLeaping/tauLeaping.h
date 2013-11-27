/* 
 * File:   tauLeaping.h
 * Author: Jernej Hartman
 *
 * Created on January 8, 2013, 1:07 PM
 */

#ifndef TAULEAPING_H
#define	TAULEAPING_H

// include the utils function that are used 
#include "utils.h"
#include <vector>
#include <cstdlib>
#include <iostream>
#include <tr1/unordered_map>
#include <string.h>
#include <algorithm>

using namespace std;
using namespace std::tr1;

typedef unordered_map<long,double> HashMap;

// a simple structure to hold all the necessary data for a reaction
struct reaction
{
   // Reaction speed
   double k; 
   // number of reactants
   short nrReactants;
   // number of products (can be zero)
   short nrProducts;
   // list of all species in the reaction
   long * species;
   // state change vector (size of v is nrReactants + nrProducts)
   // NOTE: if there are 2 or more species of the same kind, v should contain only distinct reactions
   short * v;
};
// a simple structure to hold all the necessary data for a specie
struct specie
{
    // Highest order of reaction in which specie appears as a reactant
    short hor;
    // g value of the species
    long g;
    // optimization flag which indicates whether the g value is a constant or should be computed in each step of optimized tau leap
    bool isDynamic;
    // the reaction index in which the specie appears as a reactant and has also the highest order
    long horRIndex;
    // the state change vector index of the reaction (where species is HOR) 
    long horRVIndex;
    // list of all reactions in which specie appears as a reactant
    std::vector<long> reactions;
};

class tauLeaping {
public:
    // constructor
    tauLeaping();
    // deconstructor
    virtual ~tauLeaping();
    // entry point for the simulation where all the parameters are passed to the system
    void startSimulation(long N, long M, long * x0, short vMaxLen, short ** v, long ** RS, double * k, double simTime, long omega, short recordInterval = 1, double epsilon = 0.03, short ssaTreshold = 10, long exactIter = 100, short criticalTreshold = 10);
    // call when want to cancel the processing
    void cancel();
private:
    // all reaction array
    reaction * reactions;
    // all species array (used for optimizing the performance)
    specie * species;
    // current state (size = N)
    long * x;
    // length of vector v (2nd dimension)
    short vLen;
    
    
    // number of species
    long numSpecies;
    // number of reactions
    long numReactions;
    
    // critical reaction list (contains only indices to reactions)
    vector<long> critical;
    // non critical reaction list
    vector<long> nonCritical;
    // propensity vector
    double * prop;
    // propensity sum
    double propSum;
    // total time of the simulation 
    double time;
    // number of iterations 
    long iterations;
    // how long to simulate the algorithm
    double simTime;
    // actual simulation time (in milliseconds)
    double actualSimTime;
    // record each k iteration
    short recordInterval;
    // how many iterations of exact SSA to perform
    short exactIterations;
    // critical treshold
    short criticalTreshold;
    // error control parameter (0..1)
    double epsilon;
    // multiple treshold to determine which method to use exact or explicit
    double ssaTreshold;
    // system size (omega)
    long systemSize;
    // are we using ssa or ETL? 
    bool useSSA;
    // current number of SSA iterations
    long ssaIteration;
    // are propenseties calculated_
    bool propCalc;
    // creates a array of all reactions where M is the number of reactions, v is the state change vector for each reaction, k the speed of the reaction and RS the indices of species in reaction
    void validateParameters(long N, long M, short ** v, double * k, long ** RS);
    // steps the simulation according to the modified (non-negative) tau leaping algorithm
    // Based on article: Efficient step size selection for the tau-leaping simulation method, The journal of Chemical Physics 
    // url: http://www.cs.ucsb.edu/~cse/Files/NewTau052.pdf
    double stepSimulation();
    // simulates a step for (exact) SSA algorithm
    void single_step_SSA(double & dt);
    // simulate a explicit tau leaping step
    void single_step_ETL(double & dt);
    // main simulation loop (TODO: the main run loop should implement a interrupt handler so the user can cancel the simulation.
    void run();
    // clears all the simulation results and the parameters.
    void reset();
    // calculates the propensity function for the given raction and current state vector and stores it into result
    bool calculatePropensity(const reaction reaction, const long * curState, double & result);
    // calculates the propensity functions for all the given reactions and retuns a boolean indication if there are any negative propensities
    bool calculatePropensities(const long M, const reaction * reactions, const long * curState, double *& result, double & sum);
    // updates the state vector for a single reaction 
    void updateState(const reaction R, const double k, long *& result);
    // updates the g vector for each dynamic species
    void updateG();
    // finds the highest order of reaction in which a specie appears as a reactant
    void findHOR();
    // calculates tau that satisfies the leap condition
    void getTau(double & tau);
    // prints the state with timestamp
    void printData(long * x, long dt);

};

#endif	/* TAULEAPING_H */

