/* 
 * File:   utils.h
 * Author: Jernej Hartman
 *
 * Created on January 8, 2013, 1:12 PM
 * 
 * Utils.h contains all the neccessery function to simulate tau leaping. 
 * It contains helper math functions. 
 */

#ifndef UTILS_H
#define	UTILS_H

#include <math.h>
#include <cstdlib>
#include <iostream>
#include <random>


/**
 * Computes the exponential random variable with mean mean and stores it into result.
 * @param mean - mean of the random variable
 * @param result 
 */
inline void getExpRandom(double mean, double & result)
{
    double r1 = ((double) rand() / (RAND_MAX));
    result = (mean) * log(1/r1);
}
/**
 * Computes the factorial value of the given input (value!) and stores it into result.
 * @param value 
 * @param result
 */
inline void getFactorialValue(long value, long & result)
{
    result = 1;
    while(value > 1)
    {
        result *= value;
        value--;
    }
}
/**
 * Computes the binomial symbol (n over k) and stores it into result.
 * @param n
 * @param k
 * @param result
 */
inline void getBinomialValue(long n, long k, long & result)
{
    int l = 0;
    result = 1;
    for (l = 1; l <= k; l++)
    {
        result *= (n - l + 1)/l;
    }
}
/**
 * Computes the random poisson value with mean mean and stores it into result.
 * @param mean
 * @param result
 */
inline void getPoissonValue(double mean, long & result)
{
    std::default_random_engine generator;
    std::poisson_distribution<long> distribution(mean); 
    result = distribution(generator);
}

inline void getPointProbValue(const double * prob, const long probSize, const double sum, long & result)
{
    double bound = ((double) rand() / (RAND_MAX)) * sum;
    double cur = 0;
    result = -1;
    while(cur < bound)
    {
        result++;
        cur += prob[result];
    }
}


#endif	/* UTILS_H */

