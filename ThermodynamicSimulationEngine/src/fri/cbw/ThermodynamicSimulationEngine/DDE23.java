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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.ArrayUtils; // this is a new dependency

//TODO: remove fixmes that do not need fixing
//TODO: for some equations the solver may take a looong time,
//      consider adding some progress observer visitor and/or timeout
/**
 * Java implementation of the dde23 delay differential equation (DDE) solver
 * from Matlab.
 *
 * The solver by L.F. Shampine and S. Thompson is described at
 *
 * http://www.radford.edu/~thompson/webddes/
 *
 * % The tutorial "Solving Delay Differential Equations with DDE23" includes % a
 * brief discussion of DDEs, a brief discussion of the numerical methods % used
 * by DDE23, complete solutions of many examples from the literature % that
 * illustrate how to use DDE23, and some exercises (with solutions). % % DDE23
 * was written by L.F. Shampine and S. Thompson. A detailed % discussion of the
 * numerical methods used by DDE23 can be found in % "Solving DDEs in Matlab" by
 * L.F. Shampine and S. Thompson. % % Copyright (c) 2000-2014 by L.F. Shampine
 * and S. Thompson.
 *
 * NOTES:
 *
 * Compared to the original code, this class is subject to several limitations.
 *
 * - Integration always starts at t = 0
 *
 * - Events are not supported
 *
 * - Options are not supported
 *
 * - Discontinuities in the solution cannot be specified (we don't have options)
 *
 * - The initial state history is always given as a constant vector for t < 0
 */
public class DDE23 {

    // FIXME: should this be calculated somehow?
    static final double eps = 2.2204e-16;

    private static List<Double> purgeDuplicates(List<Double> vl) {
        Collections.sort(vl);
        List<Double> vl_temp = new ArrayList<Double>();
        vl_temp.add(vl.get(0));
        for (int k = 1; k < vl.size(); k++) {
            final double fst = Math.abs(vl.get(k) - vl.get(k - 1));
            final double snd = Math.abs(vl.get(k - 1));
            if (fst > 10 * eps * snd) {
                vl_temp.add(vl.get(k));
            }
        }
        return vl_temp;
    }

    /**
     * This interface represents a first order delay differential equations set.
     *
     * The interface is modeled after
     * org.apache.commons.math3.ode.FirstOrderDifferentialEquations with the
     * difference that the method computeDerivatives additionally takes the
     * array of delayed variables Z.
     */
    public static interface FirstOrderDelayDifferentialEquations {

        /**
         * @param t current value of the independent time variable
         * @param y array containing the current value of the state vector
         * @param Z values of y at delays specified in `lags` parameter of
         * integrate()
         * @param yPrime placeholder array where to put the time derivative of
         * the state vector
         */
        public void computeDerivatives(double t, double[] y, double[][] Z, double[] yPrime);

        /**
         * Get the dimension of the problem.
         *
         * @return dimension of the problem
         */
        public int getDimension();
    }

    public static class IntegrationResult {

        double[] x;
        double[][] y;
        double[][] yp;
        private ArrayList<double[]> Yp;
        private ArrayList<double[]> Y;
        private ArrayList<Double> X;
        private ArrayList<Double> discont;

        public List<Double> getXs() {
            return X;
        }

        public List<double[]> getYs() {
            return Y;
        }

        public double[] getYAt(double x) {
            return null;
        }

        private void setDiscont(List<Double> discont) {
            this.discont = new ArrayList<Double>(discont);
        }

        private void setX(List<Double> x) {
            this.X = new ArrayList<Double>(x);
        }

        private void setY(List<double[]> y) {
            this.Y = new ArrayList<double[]>(y);
        }

        private void setYp(List<double[]> yp) {
            this.Yp = new ArrayList<double[]>(yp);
        }

        public static boolean isSorted(double[] list) {
            boolean sorted = true;
            for (int i = 1; i < list.length; i++) {
                if (list[i - 1] > list[i]) {
                    sorted = false;
                }
            }
            return sorted;
        }

        /**
         * DDEVAL Evaluates the solution computed by DDE23 at XINT. DDE23
         * returns a solution in the form of a structure SOL. DDEVAL evaluates
         * this solution at all the entries of the % vector XINT. For each K,
         * SXINT(:,K) is the solution corresponding to XINT(K) and SPXINT(:,K)
         * is the first derivative of the solution.
         *
         * DDE23 provides the independent variable x = sol.x as an ordered row
         * vector and y = sol.y as y(:,k) = y(x(k)). The cubic Hermite
         * interpolant also uses the slopes yp = sol.yp.
         *
         * The above is the original Matlab documentation. FIXME?: This function
         * is tested only for use with one element in xint array and null for
         * Spxint.
         */
        public double[][] ddeval(double[] xint, double[][] Spxint) {
            int Nx = y.length;
            int neq = y[0].length;
            int Nxint = xint.length;
            double[][] Sxint = new double[Nxint][neq];
            if (Spxint != null) { // FIXME: remove this
                Spxint = new double[Nxint][neq];
            }

            //% Make a local copy of xint that is a row vector and
            //% if necessary, sort it to match the order of x.
            double[] Xint = Arrays.copyOf(xint, xint.length);
            int xdir = (int) Math.signum(x[x.length - 1] - x[0]);
            boolean had2sort = !isSorted(xint);
            if (had2sort) {
                Arrays.sort(Xint);
                if (xdir < 0) {
                    ArrayUtils.reverse(Xint);
                }
            }

            //% Using the sorted version of xint, test for illegal values.
            if ((xdir * (Xint[0] - x[0]) < 0) || (xdir * (Xint[Xint.length - 1] - x[x.length - 1]) > 0)) {
                String msg = String.format("Attempting to evaluate the solution outside the interval\n"
                        + "[%e, %e] where it is defined.\n", x[0], x[Nx]);
                Logger.getGlobal().log(Level.SEVERE, msg);
            }

            int evaluated = 0;
            int bottom = 0; // zero based
            while (evaluated < Nxint) {
                //% Find subinterval containing the next entry of Xint.
                //% There is a special case when an entry equals x(end).
                //% NOTE If successive entries in x are the same and one
                //%      should be selected as bottom, it will be the one
                //%      of largest index except when it would be the last
                //%      one.  This avoids potential difficulty with
                //%      interpolating in an interval of zero length except
                //%      when the last two values are the same, which is
                //%      not allowed of the input data.

                List<Integer> Index = new ArrayList<Integer>();
                for (int k = bottom; k < x.length; k++) {
                    if (xdir * (Xint[evaluated] - x[k]) >= 0) { // had evaluated+1 here
                        // Index contains indices of elements of x <= Xint[evaluated]
                        Index.add(k);
                    }
                }
                bottom = Math.min(bottom + Index.get(Index.size() - 1), Nx - 2); // -2 not the last one,
                //we want to be able to do bottom+1

                //% Find all the entries of Xint contained in this subinterval:
                List<Integer> index = new ArrayList<Integer>();
                for (int k = evaluated; k < Xint.length; k++) { // remove +1, zero based
                    if (xdir * (Xint[k] - x[bottom + 1]) <= 0) {
                        index.add(k);
                    }
                }

                //% Vectorized evaluation of the interpolant for all these entries:
                double h = x[bottom + 1] - x[bottom];
                if (h <= 0) {
                    String msg = String.format("%d, %f", bottom, x[bottom]);
                    Logger.getGlobal().log(Level.SEVERE, msg);
                    throw new RuntimeException(msg);
                }

                double[] t = new double[index.size()];
                for (int k = 0; k < index.size(); k++) {
                    t[k] = (Xint[evaluated + k] - x[bottom]) / h;
                }
                double[] fn = y[bottom];
                double[] fpn = yp[bottom];
                double[] fnp1 = y[bottom + 1];
                double[] fpnp1 = yp[bottom + 1];
                double[] slope = new double[neq];
                double[] c = new double[neq];
                double[] d = new double[neq];
                for (int k = 0; k < neq; k++) {
                    slope[k] = (fnp1[k] - fn[k]) / h;
                    c[k] = (3 * slope[k] - 2 * fpn[k] - fpnp1[k]) * h;
                    d[k] = (fpn[k] + fpnp1[k] - 2 * slope[k]) * h;
                }

                double[] t2 = new double[t.length];
                double[] t3 = new double[t.length];

                for (int k = 0; k < index.size(); k++) {
                    t2[k] = t[k] * t[k];
                    t3[k] = t2[k] * t[k];
                }

                for (int k = 0; k < neq; k++) {
                    for (int l = 0; l < index.size(); l++) {
                        Sxint[evaluated + l][k] = d[k] * t3[l] + c[k] * t2[l] + fpn[k] * t[l] * h + fn[k];
                        if (Spxint != null) {
                            Spxint[evaluated + l][k] = (3 * d[k] * t2[l] + 2 * c[k] * t[l] + fpn[k] * h) / h;
                        }
                    }
                }

                evaluated = evaluated + index.size();
            }

            if (had2sort) {     //% Restore the order of xint in the output.
                double v;
                double[] tmp;
                for (int i = 0; i < xint.length; i++) {
                    v = Xint[i];
                    int j = Arrays.binarySearch(Xint, v);
                    tmp = Sxint[i];
                    Sxint[i] = Sxint[j];
                    Sxint[j] = tmp;
                    if (Spxint != null) {
                        tmp = Spxint[i];
                        Spxint[i] = Spxint[j];
                        Spxint[j] = tmp;
                    }
                }
            }
            return Sxint;
        }
    }

    /**
     *
     * @param ddes delay differential equation
     * @param lags array of time delay values, does not have to be sorted
     * @param history values of variables for t < 0
     * @param t0 time to start the integration, it should be 0
     * @param tfinal the end time
     * @return Class representing the result of the integration
     */
    public static IntegrationResult integrate(FirstOrderDelayDifferentialEquations ddes,
            double[] lags,
            double[] history,
            double t0,
            double tfinal) {
        IntegrationResult sol = new IntegrationResult();

//% Initialize statistics.
        int nsteps = 0;
        int nfailed = 0;
        int nfevals = 0;

        if (tfinal <= t0) {
            throw new IllegalArgumentException("Must have t0 < tfinal.");
        }

        double[] temp;
        temp = new double[history.length];
        System.arraycopy(history, 0, temp, 0, history.length);

        double[] y0;
        y0 = new double[temp.length];
        System.arraycopy(temp, 0, y0, 0, temp.length);
        int maxlevel = 4;

        double t = t0;
        double[] y;
        y = new double[y0.length];
        System.arraycopy(y0, 0, y, 0, y0.length);
        int neq = ddes.getDimension();

//% If solving a DDE, locate potential discontinuities. We need to step to
//% each of the points of potential lack of smoothness. Because we start at
//% t0, we can remove it from discont.  The solver always steps to tfinal,
//% so it is convenient to add it to discont.
        List<Double> discont = new ArrayList<Double>();
        double minlag = Double.POSITIVE_INFINITY;
        if (lags.length == 0) {
            discont.add(tfinal);
        } else {
            for (int i = 0; i < lags.length; i++) {
                if (lags[i] < minlag) {
                    minlag = lags[i];
                }
            }
            if (minlag <= 0) {
                throw new IllegalArgumentException("The lags must all be positive.");
            }
            List<Double> vl = new ArrayList<Double>();
            vl.add(t0);

            for (int level = 1; level < maxlevel; level++) { // it is not <=, comparing discont with matlab
                List<Double> vlp1 = new ArrayList<Double>();
                for (int i = 0; i < vl.size(); i++) {
                    for (int x = 0; x < lags.length; x++) { // should probably be from 1 // probably not
                        vlp1.add(vl.get(i) + lags[x]);
                    }
                }
                //% Restrict to tspan.
                vl.clear(); // FIXME: this looks like a mistake
                for (double x : vlp1) {
                    if (x <= tfinal) {
                        vl.add(x);
                    }
                    if (vl.isEmpty()) {
                        break;
                    }
                    int nvl = vl.size();
                    if (nvl > 1) //% Purge duplicates in vl.
                    {
                        vl = purgeDuplicates(vl);
                    }
                }
                discont.addAll(vl); // FIXME: make sure this is where it should be
            }
            if (discont.size() > 1) {
                //% Purge duplicates.
                discont = purgeDuplicates(discont);
            }
        }

//% Add tfinal to the list of discontinuities if it is not already included.
        int end = discont.size() - 1;
        if (Math.abs(tfinal - discont.get(end)) <= 10 * eps * Math.abs(tfinal)) {
            discont.set(end, tfinal);
        } else {
            discont.add(tfinal);
        }
        sol.setDiscont(discont);
//% Discard t0 and discontinuities in the history.
        List<Integer> indices = new ArrayList<Integer>();
        for (int i = 0; i < discont.size(); i++) {
            if (discont.get(i) <= t0) {
                indices.add(i);
            }
        }
        discont.removeAll(indices);

        int nextdsc = 0; // zero based
        List<Integer> ndxdsc = new ArrayList<Integer>();
        ndxdsc.add(1);

//% Get options, and set defaults.
        double rtol = 1e-3;

//        if (rtol < 100 * eps) {
//           rtol = 100 * eps;
//           warning(['RelTol has been increased to ' num2str(rtol) '.']);
//        }
        double atol = 1e-6;
        double threshold = atol / rtol;

//% By default, hmax is 1/10 of the interval of integration.
        double hmax = Math.min((tfinal - t), Math.abs(0.1 * (tfinal - t)));
        if (hmax <= 0) {
            throw new IllegalArgumentException("MaxStep must be greater than zero.");
        }
        double minstep = 0;
        boolean rept_minh = false;

        boolean printstats = true; // FIXME: for debugging

//% Allocate storage for output arrays and initialize them.
        int chunk = Math.min(100, (int) Math.floor((2 ^ 13) / neq));

        double[] tout = new double[chunk];
        double[][] yout = new double[chunk][neq]; // flip to C style
        double[][] ypout = new double[chunk][neq];
        int nout = 1; // FIXME: should be 0-based? // probably not
        tout[nout] = t;
        for (int k = 0; k < neq; k++) {
            for (int l = 0; l < chunk; l++) {
                yout[l][k] = y[k]; // FIXME: think about this
            }
        }

//% Initialize method parameters.
        double pow = 1.0 / 3;
        double[][] B = { // transposed
            {1.0 / 2, 0, 0, 0},
            {0, 3.0 / 4, 0, 0, 0},
            {2.0 / 9, 1.0 / 3, 4.0 / 9.0, 0}
        };
        double[] E = {-5.0 / 72, 1.0 / 12, 1.0 / 9, -1.0 / 8};

        double[][] f = new double[4][neq]; // flipped to C indexing

//% Evaluate initial history at t0 - lags.
        double[][] emptyarr = new double[0][];
        double[] fakeX = {t}; // FIXME: try to understand what is happening here with t/X
        double[][] fakeY = {y}; // FIXME: DTTO
        double[][] Z = lagvals(t, lags, history, fakeX, fakeY, emptyarr);

        double[] f0 = new double[neq];
        ddes.computeDerivatives(t, y, Z, f0);
        for (int k = 0; k < neq; k++) {
            ypout[nout][k] = f0[k]; // FIXME: think about this // should be correct now
        }
        nfevals = nfevals + 1;                  //% stats
        int m = f0.length;
        if (m != neq) {
            // FIXME: find better class to throw?
            throw new IllegalArgumentException("returned derivative and history vectors of different lengths.");
        }

        double hmin = 16 * eps * t;

//% Compute an initial step size h using y'(t).
        double h = Math.min(hmax, tfinal - t0);
        double n = Double.NEGATIVE_INFINITY; // compute norm(xk, inf)
        for (int k = 0; k < f0.length; k++) {
            double xk = Math.abs(f0[k] / Math.max(Math.abs(y[k]), threshold));
            if (xk > n) {
                n = xk;
            }
        }
        double rh = n / (0.8 * Math.pow(rtol, pow));
        if (h * rh > 1) {
            h = 1.0 / rh; // NOTE: used to have 1 / rh there, did not work :(
        }
        h = Math.max(h, hmin);

//% Make sure that the first step is explicit so that the code can
//% properly initialize the interpolant.
        h = Math.min(h, 0.5 * minlag);

        System.arraycopy(f0, 0, f[0], 0, neq);

//% THE MAIN LOOP
        double[] last_y = new double[neq];
        boolean done = false;
        while (!done) {
            //% By default, hmin is a small number such that t+hmin is only slightly
            //% different than t.  It might be 0 if t is 0.
            hmin = 16 * eps * t;
            h = Math.min(hmax, Math.max(hmin, h));    //% couldn't limit h until new hmin

            //% Adjust step size to hit discontinuity. tfinal = discont(end).
            boolean hitdsc = false;
            double distance = discont.get(nextdsc) - t;
            if (Math.min(1.1 * h, hmax) >= distance) {          //% stretch
                h = distance;
                hitdsc = true;
            } else if (2 * h >= distance) {                  //% look-ahead
                h = distance / 2;
            }
            if (!hitdsc && (minlag < h) && (h < 2 * minlag)) {
                h = minlag;
            }

            //% LOOP FOR ADVANCING ONE STEP.
            double tnew;
            double ynew[] = new double[neq];
            boolean itfail;
            boolean nofailed = true;                      //% no failed attempts
            double err;
            while (true) {
                double[][] hB = new double[3][4]; // dimensions of B
                for (int k = 0; k < 3; k++) {
                    for (int l = 0; l < 4; l++) {
                        hB[k][l] = h * B[k][l];
                    }
                }
                double t1 = t + 0.5 * h;
                double t2 = t + 0.75 * h;
                tnew = t + h;

                //% If a lagged argument falls in the current step, we evaluate the
                //% formula by iteration. Extrapolation is used for the evaluation
                //% of the history terms in the first iteration and the tnew,ynew,
                //% ypnew of the current iteration are used in the evaluation of
                //% these terms in the next iteration.
                int maxit;
                if (minlag < h) {
                    maxit = 5;
                } else {
                    maxit = 1;
                }
                // FIXME: maybe it should be implemented as appending to a List
                double[] X = new double[nout + 1]; // looks like +1 is unavoidable. nout is the largest index, it is 1 based
                System.arraycopy(tout, 0, X, 0, nout + 1); // so need nout+1 length
                double[][] Y = new double[nout + 1][neq];
                System.arraycopy(yout, 0, Y, 0, nout + 1);
                double[][] YP = new double[nout + 1][neq];
                System.arraycopy(ypout, 0, YP, 0, nout + 1);

                itfail = false;
                for (int iter = 1; iter <= maxit; iter++) { //FIXME: 0-based? // no, from 1, maybe <= // try <=
                    double[] vecmul1 = new double[neq];
                    for (int k = 0; k < neq; k++) { // FIXME: merge the loops, it should be possible // it is not
                        for (int l = 0; l < 4; l++) {
                            vecmul1[k] += f[l][k] * hB[0][l];
                        }
                    }
                    double yt1[] = new double[neq];
                    for (int k = 0; k < f[0].length; k++) { // changed from f.length
                        yt1[k] = y[k] + vecmul1[k]; // FIXME: indices?
                    }

                    Z = lagvals(t1, lags, history, X, Y, YP);
                    ddes.computeDerivatives(t1, yt1, Z, f[1]);

                    double[] vecmul2 = new double[neq];
                    for (int k = 0; k < neq; k++) { // FIXME: merge the loops, it should be possible // it is not
                        for (int l = 0; l < 4; l++) {
                            vecmul2[k] += f[l][k] * hB[1][l];
                        }
                    }
                    double yt2[] = new double[neq];
                    for (int k = 0; k < f[0].length; k++) { // changed from f.length
                        yt2[k] = y[k] + vecmul2[k];
                    }

                    Z = lagvals(t2, lags, history, X, Y, YP);
                    ddes.computeDerivatives(t2, yt2, Z, f[2]);

                    double[] vecmul3 = new double[neq];
                    for (int k = 0; k < neq; k++) {
                        for (int l = 0; l < 4; l++) {
                            vecmul3[k] += f[l][k] * hB[2][l];
                        }
                    }
                    for (int k = 0; k < f[0].length; k++) {
                        ynew[k] = y[k] + vecmul3[k];
                    }

                    Z = lagvals(tnew, lags, history, X, Y, YP);
                    ddes.computeDerivatives(tnew, ynew, Z, f[3]);

                    nfevals = nfevals + 3;
                    if (maxit > 1) {
                        if (iter > 1) {
                            double errit = Double.NEGATIVE_INFINITY;
                            for (int k = 0; k < neq; k++) { // another norm(erritk, inf)
                                // missed this norm the first time
                                double erritk = Math.abs((ynew[k] - last_y[k]) / Math.max(Math.max(Math.abs(y[k]), Math.abs(ynew[k])), threshold));
                                if (erritk > errit) {
                                    errit = erritk;
                                }
                            }

                            if (errit <= 0.1 * rtol) {
                                break;
                            }
                        }
                        //% Use the tentative solution at tnew in the evaluation of the
                        //% history terms of the next iteration.
                        X = Arrays.copyOf(X, X.length + 1);
                        X[X.length - 1] = tnew;

                        Y = Arrays.copyOf(Y, Y.length + 1);
                        Y[Y.length - 1] = ynew;

                        YP = Arrays.copyOf(YP, YP.length + 1);
                        YP[YP.length - 1] = f[3];

                        System.arraycopy(ynew, 0, last_y, 0, ynew.length); // had switched last_y and ynew
                        itfail = (iter == maxit);
                    }
                }

                // FIXME: matrix multiplication?
                // had bug: first compute n, then do h*n, not the other way
                double[] vecmul = new double[neq];
                for (int l = 0; l < neq; l++) {
                    for (int o = 0; o < E.length; o++) {
                        vecmul[l] += (f[o][l] * E[o]);
                    }
                }
                n = Double.NEGATIVE_INFINITY;
                for (int k = 0; k < neq; k++) {
                    // had bug: infty norm is in abs value
                    // another ona, had multiplication instead of division. took me two hours to realize :(
                    double nk = Math.abs(vecmul[k] / Math.max(Math.max(Math.abs(y[k]), Math.abs(ynew[k])), threshold)); // FIXME: indexing of f
                    if (nk > n) {
                        n = nk;
                    }
                }

                //% Estimate the error.
                err = h * n;

                //% If h <= minstep, adjust err so that the step will be accepted.
                //% Note that minstep < minlag, so maxit = 1 and itfail = false.  Report
                //% once that a step of minimum size was taken.
                if (h <= minstep) {
                    err = Math.min(err, rtol);
                    if (rept_minh) {
                        Logger.getGlobal().log(Level.INFO, "Steps of size MinStep were taken.");
                        rept_minh = false;
                    }
                }

                //% Accept the solution only if the weighted error is no more than the
                //% tolerance rtol.  Estimate an h that will yield an error of rtol on
                //% the next step or the next try at taking this step, as the case may be,
                //% and use 0.8 of this value to avoid failures.
                if ((err > rtol) || itfail) {                        //% Failed step
                    nfailed = nfailed + 1;
                    Logger logger = Logger.getGlobal();
                    if (h <= hmin) {
                        String msg = String.format("Failure at t=%e.  Unable to meet integration "
                                + "tolerances without reducing the step size below "
                                + "the smallest value allowed (%e) at time t.\n", t, hmin);
                        logger.log(Level.WARNING, msg);
                        if (printstats) {
                            logger.log(Level.INFO, "%g successful steps", nsteps);
                            logger.log(Level.INFO, "%g failed attempts", nfailed);
                            logger.log(Level.INFO, "%g function evaluations", nfevals);
                        }
                        //% Trim output arrays, place in solution structure, and return.
                        sol = trim(sol, history, nout, tout, yout, ypout, ndxdsc);
                        return sol;
                    }

                    if (itfail) {
                        h = 0.5 * h;
                        if (h < 2 * minlag) {
                            h = minlag;
                        }
                    } else if (nofailed) {
                        nofailed = false;
                        h = Math.max(hmin, h * Math.max(0.5, 0.8 * Math.pow(rtol / err, pow)));
                    } else {
                        h = Math.max(hmin, 0.5 * h);
                    }
                    hitdsc = false;
                } else {                            //% Successful step
                    break;
                }
            }
            nsteps = nsteps + 1;                  //% stats

            //% Advance the integration one step.
            t = tnew;
            y = ynew;
            System.arraycopy(f[3], 0, f[0], 0, neq);                     //% BS(2,3) is FSAL.
            nout = nout + 1;

            // reallocate arrays
            if (nout > tout.length - 1) {
                tout = Arrays.copyOf(tout, tout.length + chunk);

                yout = Arrays.copyOf(yout, yout.length + chunk);
                ypout = Arrays.copyOf(ypout, ypout.length + chunk);
                // allocate the second dimensions
                int upto = yout.length;
                for (int k = yout.length - chunk; k < upto; k++) {
                    yout[k] = new double[neq];
                }
                upto = ypout.length;
                for (int k = ypout.length - chunk; k < upto; k++) {
                    ypout[k] = new double[neq];
                }
            }
            tout[nout] = t;
            for (int k = 0; k < neq; k++) {
                yout[nout][k] = y[k];
                ypout[nout][k] = f[0][k];
            }

            //% If there were no failures, compute a new h.
            if (nofailed && !itfail) {
                //% Note that h may shrink by 0.8, and that err may be 0.
                double temp2 = 1.25 * Math.pow(err / rtol, pow);
                if (temp2 > 0.2) {
                    h = h / temp2;
                } else {
                    h = 5 * h;
                }
                h = Math.max(h, minstep); //FIXME: NetBeans says value never used
            }

            //% Have we hit tfinal = discont(end)?
            if (hitdsc) {
                nextdsc = nextdsc + 1;
                done = nextdsc > discont.size() - 1;
                if (!done) {
                    ndxdsc.add(nout);
                }
            }
        }
//% Successful integration:
        if (printstats) {
            if (printstats) {
                Logger logger = Logger.getGlobal();
                logger.log(Level.INFO, "%g successful steps", nsteps);
                logger.log(Level.INFO, "%g failed attempts", nfailed);
                logger.log(Level.INFO, "%g function evaluations", nfevals);
            }
        }

//% Trim output arrays, place in solution structure, and return.
        sol = trim(sol, history, nout, tout, yout, ypout, ndxdsc);
        return sol;
//% End of function dde23.
    }

    /**
     * Copies the data given in second to last parameter over into a
     * IntegrationResult instance.
     *
     * @param sol
     * @param history
     * @param nout
     * @param tout
     * @param yout
     * @param ypout
     * @param ndxdsc
     * @return
     */
    private static IntegrationResult trim(IntegrationResult sol, double[] history, int nout, double[] tout, double[][] yout, double[][] ypout, List<Integer> ndxdsc/*, haveeventfun, teout, yeout, ieout*/) {
        //% Trim output arrays and place in solution structure.
        List<Double> x = new ArrayList<Double>(nout + 1);
        List<double[]> y = new ArrayList<double[]>(nout + 1);
        List<double[]> yp = new ArrayList<double[]>(nout + 1);
        //List<Double> lndxdsc = new ArrayList<Double>(ndxdsc);

        for (int k = 1; k < nout + 1; k++) {
            //System.out.println(String.format("x.size is %d, k is %d, nout is %d", x.size(), k, nout));
            x.add(tout[k]);
            y.add(yout[k]);
            yp.add(ypout[k]);
        }

        sol.setX(x);
        sol.setY(y);
        sol.setYp(yp);
        //sol.setNdxdsc(lndxdsc);

        sol.x = Arrays.copyOfRange(tout, 1, nout + 1);
        sol.y = Arrays.copyOfRange(yout, 1, nout + 1);
        sol.yp = Arrays.copyOfRange(ypout, 1, nout + 1);
        return sol;
    }

    /**
     *  //% For each I, Z(:,I) is the solution corresponding to TNOW - LAGS(I).
     * //% This solution can be computed in several ways: the initial history,
     * //% interpolation of the computed solution, extrapolation of the computed
     * //% solution, interpolation of the computed solution plus the tentative
     * //% solution at the end of the current step. The various ways are set //%
     * in the calling program when X,Y,YP are formed.
     *
     * @param tnow
     * @param lags
     * @param history
     * @param X
     * @param Y
     * @param YP
     * @return
     */
    private static double[][] lagvals(double tnow, double[] lags, double[] history, double[] X, double[][] Y, double[][] YP) {
        //% No lags corresponds to an ODE.
        if (lags.length == 0) {
            double[][] Z = null;
            return Z;
        }

        //% Typically there are few lags, so it is reasonable to process
        //% them one at a time.  NOTE that the lags may not be ordered and
        //% that it is necessary to preserve their order in Z.
        double[] xint = new double[lags.length];
        for (int k = 0; k < lags.length; k++) {
            xint[k] = tnow - lags[k];
        }
        int Nxint = xint.length;
        int neq = Y[0].length; // C indexing
        // FIXME: zeros
        double[][] Z = new double[Nxint][neq]; // flipped to C style

        for (int j = 0; j < Nxint; j++) {
            if (xint[j] < X[0]) {
                for (int k = 0; k < neq; k++) {
                    Z[j][k] = history[k]; // FIXME: history k or j? // almost sure its k
                }
            } else {
                //% Find n for which X(n) <= xint(j) <= X(n+1).  xint(j) bigger
                //% than X(end) are evaluated by extrapolation, so n = end-1 then.
                int n = -1;
                for (int k = 1; k < X.length - 1; k++) { // not including last X // X is 1-based
                    if (xint[j] >= X[k]) { // FIXME: this is probably wrong // may be fixed
                        n = k;
                    }
                }
                for (int k = 0; k < neq; k++) {
                    Z[j][k] = hermite(
                            xint[j],
                            X[n],
                            //Y[k][n],
                            Y[n][k],
                            //YP[k][n],
                            YP[n][k],
                            X[n + 1],
                            //Y[k][n + 1],
                            Y[n + 1][k],
                            //YP[k][n + 1]
                            YP[n + 1][k]
                    );
                }

            }
        }
        return Z; // FIXME: this might be on wrong level
    }

    /**
     * Interpolates between two values by using a Hermite polynomial.
     *
     * @param x
     * @param xn
     * @param yn
     * @param ypn
     * @param xnp1
     * @param ynp1
     * @param ypnp1
     * @return the interpolated value
     */
    private static double hermite(double x, double xn, double yn, double ypn, double xnp1, double ynp1, double ypnp1) {
        double h = xnp1 - xn;
        double s = (x - xn) / h;
        double A1 = (1 + 2 * s) * Math.pow(s - 1, 2);
        double A2 = (3 - 2 * s) * Math.pow(s, 2);
        double B1 = h * s * Math.pow(s - 1, 2);
        double B2 = h * (s - 1) * Math.pow(s, 2);
        return A1 * yn + A2 * ynp1 + B1 * ypn + B2 * ypnp1;
    }
}
