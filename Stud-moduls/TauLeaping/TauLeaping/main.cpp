/* 
 * File:   main.cpp
 * Author: Jernej Harmtan
 *
 * Created on January 10, 2013, 5:47 AM
 */

#include <cstdlib>
#include "tauLeaping.h"
#include <iostream>

using namespace std;
/*
 * 
 */


int main(int argc, char** argv) {
    tauLeaping * engine = new tauLeaping();
    // this is a blocking function? should be in a thread or sumfin
    
    // R1: S1 + S2 => S3
    // R2: S3 => S4
    
    double simTime = 100;
    long omega = 10;
    long N = 4;
    long M = 2;
    long * x0 = new long[N];
    x0[0] = 10, x0[1] = 10, x0[2] = 1000, x0[3] = 0; //{1, 10, 0 ,0};
    
    double * k = new double[2]; //= {0.04, 0.1};
    k[0] = 0.01;
    k[1] = 0.5;
   
    
   // short * v [2][3];//{{-1,-1,1},{-1,1,0}};
   // short * RS [2][3];//{{-1,-1,1},{-1,1,0}};
   
    short ** v = new short*[2];
    long ** RS = new long*[2];
    for (int i = 0 ; i < 2; i++)
    {
        v[i] = new short[3];
        RS[i] = new long[3];
    }
    short mOne = -1;
    short pOne = 1;
    short zero = 0;
    short pThree = 3;
    short pTwo = 2;
    
    v[0][0] = mOne, v[0][1] = mOne, v[0][2] = pOne;
    v[1][0] = mOne, v[1][1] = pOne, v[1][2] = zero;
   
    RS[0][0] =  zero, RS[0][1] = pOne, RS[0][2] = pTwo;
    RS[1][0] =  pTwo, RS[1][1] = ( pThree), RS[1][2] = zero;
    short vLen = 3;
    engine->startSimulation(N, M, x0, vLen, v, RS,k, simTime,omega);
    return 0;
}
