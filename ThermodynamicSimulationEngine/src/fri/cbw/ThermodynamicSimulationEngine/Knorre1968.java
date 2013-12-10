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

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;

/**
 * Numerical simulation of an experiment done by Knorre in 1968.
 */
public class Knorre1968 {

    public void calculate() {
        PrintWriter writer = null;

        //DDESystem system = new DDESystem(0.03, 0.2, 0.1, 0.8); // I have no idea where to get these, values are sortof random here
        // probably better set of values
        DDESystem system = new DDESystem(1.54542715e-04/1000, 1.54542715e-04/1000, 3.66546278e-02/1000, 4.26454251e-02/1000);

        DDE23.IntegrationResult result = DDE23.integrate(system, system.delays, system.initial, 0, 256);
        System.out.println("possibly break here with debugger");
        {
            try {
                writer = new PrintWriter("/tmp/Knorre1968.csv", "UTF-8");
                for (int i = 0; i < result.x.length; i++) {
                    writer.print(result.x[i]);
                    for (int j = 0; j < result.y[0].length; j++) {
                        writer.print(",");
                        writer.print(result.y[i][j]);
                    }

                    writer.println();
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Knorre1968.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(Knorre1968.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                writer.close();
            }
        }
        {
            try {
                writer = new PrintWriter("/tmp/Knorre1968fracoccup.csv", "UTF8");
                for (int i = 0; i < system.fractionaloccupancyvalue.size(); i++) {
                    writer.print(system.fractionaloccupancytime.get(i));
                    writer.print(",");
                    writer.print(system.fractionaloccupancyvalue.get(i));
                    writer.println();
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Knorre1968.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(Knorre1968.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                writer.close();
            }
        }
    }

    class DDESystem implements DDE23.FirstOrderDelayDifferentialEquations {

        // for debugging
        public List<Double> fractionaloccupancyvalue = new ArrayList<Double>();
        public List<Double> fractionaloccupancytime = new ArrayList<Double>();

        // constants
        /**
         * CRP concentration
         */
        double CRP = 2.6; // uM
        /**
         * Total repressor concentration
         */
        double R_T = 2.9e-2; // uM
        /**
         * Equilibrium dissociation constant between CRP and cAMP
         */
        double K_CAP = 3.0; // uM
        /**
         * Time delay between transcription initiation and appearance of a lacZ
         * ribosome binding site
         */
        double tau_B = 5.1e-3; // min
        /**
         * Time delay between transcription initiation and appearance of a lacY
         * ribosome binding site
         */
        double tau_P = 0.1; // min
        /**
         * Growth rate
         */
        double mu = 0.02; // min^{-1}
        /**
         * mRNA degradation rate
         */
        double xi_M = 0.46; // min^{-1}
        /**
         * lac permease degradation rate
         */
        double xi_P = 0.01; // min^{-1}
        /**
         * lacY mRNA translation initiation rate
         */
        double k_P = 18.8; // min^{-1}
        /**
         * cAMP excretion and degradation rate
         */
        double xi_cAMP = 2.1; // min^{-1}
        /**
         * β-galactosidase degradation rate
         */
        double xi_B = 8.33e-4; // min^{-1}
        /**
         * lacZ mRNA translation initiation rate
         */
        double k_B = 18.8; // min^{-1}
        /**
         * Time delay due to translation of gene lacY
         */
        double T_P = 0.42; // min
        /**
         * Time delay due to translation of gene lacZ
         */
        double T_B = 1.0; // min

        /**
         * Repressor-allocatose dissociation constant
         */
        double K_A = 1.0; // uM

        /**
         * lactose transport constant for inhibition by glucose
         */
        double PhiG1 = 2.71e2; // FIXME: I am putting here the value of PhiG2 since PhiG1 is not in the article.
        /**
         * Lactose transport constant for inhibition by glucose
         */
        double PhiG2 = 2.71e2; // uM
        /**
         * Lactose transport rate constant
         */
        double psiL1 = 1.08e3; // min^{-1}
        /**
         * Lactose transport saturation constant
         */
        double PhiL1 = 5.0e-2; // uM
        /**
         * Lactose hydrolysis rate
         */
        double phiL2 = 3.60e3; // min^{-1}
        /**
         * Lactose hydrolysis saturation constant
         */
        double PhiL2 = 1.4e3; // uM;

        /**
         * cAMP synthesis rate constant
         */
        double phicAMP = 5.5; // uM min^{-1}
        /**
         * cAMP synthesis saturation constant
         */
        double PhicAMP = 40.0; // uM

        /**
         * Allolactose degradation rate constant
         */
        double xi_A = 0.0; // min^{-1}

        /**
         * Promoter concentration
         */
        double P1 = 5.0e-3; // uM

        /**
         * Transcription initiation rate
         */
        double k_m = 0.18;

        // variables // will need to move this up so it is visible
        int M_B = 0; // TODO: use enum?
        int M_P = 1;
        int B = 2;
        int P = 3;
        int cAMP_T = 4;
        int A_T = 5;

        int idx_tau_B = 0;
        int idx_tau_P = 1;
            //int cAMP = 6; // FIXME: should this be here?

        // something in between
        /**
         * extracellular glucose concentration
         */
        double G_E;
        /**
         * lactose concentration in the external medium
         */
        double L_E;

        public double[] delays;
        public double[] initial;

        public DDESystem(double mb, double mp, double b, double p) {
            delays = new double[2];
            delays[idx_tau_B] = tau_B;
            delays[idx_tau_P] = tau_P;
            initial = new double[6];
            initial[M_B] = mb;
            initial[M_P] = mp;
            initial[B] = b;
            initial[P] = p;
            initial[cAMP_T] = 0.0;
            initial[A_T] = 0.0;
        }

        @Override
        public void computeDerivatives(double t, double[] vars, double[][] Z, double[] yPrime) {
            double v_cAMP = phicAMP * (PhicAMP / (G_E + PhicAMP)); // FIXME: make sure I havent mixed this up
            double L_T = 2.0 * vars[A_T];
            double vL1 = psiL1 * ((L_E / (L_E + PhiL1)) * (PhiG1 / (PhiG1 + G_E)) - (L_T / (L_T + PhiL1))) * vars[P];
            double vL2 = psiL1 * (L_T / (L_T + PhiL2)) * vars[B];

            double psi_tau_B = P1 * k_m * fractional(Z, idx_tau_B);
            double psi_tau_P = P1 * k_m * fractional(Z, idx_tau_P);

            // log this
            fractionaloccupancyvalue.add(psi_tau_B);
            fractionaloccupancytime.add(t - tau_B);
            fractionaloccupancyvalue.add(psi_tau_P);
            fractionaloccupancytime.add(t - tau_P);

            // FIXME this line is repeated in fractional()
            double cAMP = (vars[cAMP_T] - K_CAP - 2 * CRP) / 2.0 + (1.0 / 2) * Math.sqrt(Math.pow(vars[cAMP_T] - K_CAP - 2 * CRP, 2) + 4 * vars[cAMP_T] * K_CAP);

            yPrime[M_B] = psi_tau_B - (mu + xi_M) * vars[M_B];
            yPrime[M_P] = psi_tau_P - (mu + xi_M) * vars[M_P];
            yPrime[B] = 1.0 / 4 * k_B * vars[M_B] * T_B - (mu + xi_B) * vars[B];
            yPrime[P] = k_P * vars[M_P] * T_P - (mu + xi_P) * vars[P];
            yPrime[cAMP_T] = v_cAMP - xi_cAMP * cAMP - mu * vars[cAMP_T];
            yPrime[A_T] = (vL1 - vL2) / 2 - (xi_A + mu) * vars[A_T];
        }

        // FIXME: in the paper this also takes RNAP concentrations // actually, thats a constant
        double mRNAP = 3.0; // uM

        private double fractional(double[][] Z, int tau_) {
            //TYPO?: replaced Z[cAMP][tau_] with Z[cAMP_T][tau_]
            final double[] ylag = Z[tau_];
            double cAMP = (ylag[cAMP_T] - K_CAP - 2 * CRP) / 2.0 + (1.0 / 2) * Math.sqrt(Math.pow(ylag[cAMP_T] - K_CAP - 2 * CRP, 2) + 4 * ylag[cAMP_T] * K_CAP);
            double CAP = (2 * K_CAP * cAMP) / Math.pow(K_CAP + cAMP, 2) * CRP;
            double A = (ylag[A_T] - K_A - 4 * R_T) / 2.0 + 1.0 / 2.0 * Math.sqrt(Math.pow(ylag[A_T] - K_A - 4 * R_T, 2) + 4 * ylag[A_T] * K_A);
            double R = Math.pow(K_A / (K_A + A), 4) * R_T;

            float deltaG_2p = -10.20f;
            float deltaG_cp = -1.59f;
            float deltaG_1c = -10.68f;
            float deltaG_3c = -8.58f;
            float deltaG_3r = -16.97f;
            float deltaG_4r = -14.21f;
            float deltaG_1r = -12.11f;
            float deltaG_34 = -1.0f;
            float deltaG_13 = -7.70f;
            float deltaG_14 = -6.78f;

            int tnapNumber = (int) Math.round((mRNAP * 1e-6) * (8.0e-16) * 6.022e23); // explained bellow at setRegMolNumber
            float genomeBg = (int) 5e9; // FIXME: play with this number and see what it does?
            TranscriptionUnit unit = new TranscriptionUnit(tnapNumber, deltaG_2p, genomeBg);
            int O3 = 0;
            int C1 = 1;
            int P1 = 2;
            int O1 = 3;
            int C2 = 4;
            int O2 = 5;

            unit.addRegulator("CAP", 0.0f); // paper says nothing, assuming zero
            unit.addRegulator("cAMP", deltaG_cp);

            // site1 : O3, C1
            unit.addBindingSite(O3, "CAP", deltaG_1r);
            unit.addBindingSite(C1, "cAMP", deltaG_1c);
            unit.addForbiddenEvent(new TreeSet<Integer>(Arrays.asList(O3, C1)));

            // site2 : P1
            // unit.addBindingSite(P1, "", );
            // site3 : O1, C2
            unit.addBindingSite(O1, "CAP", deltaG_3r);
            unit.addBindingSite(C2, "cAMP", deltaG_3c);
            unit.addForbiddenEvent(new TreeSet<Integer>(Arrays.asList(O1, C2)));

            // site4 : O2
            unit.addBindingSite(O2, "CAP", deltaG_4r);

            // interaction energies
            // unit.addForbiddenEvent(new TreeSet(Arrays.asList(O1, O3, P1))); // RNAP is separate, not a regulator
            // we need to subtract the energy of binding the second of the repressors, because that is how they do it in the paper
            unit.addInteractGroup(new TreeSet<Integer>(Arrays.asList(O1, O3)), deltaG_13 - deltaG_3r, Float.POSITIVE_INFINITY); // TYPO: they say int energy for sites 1,2 // it also forbids the binding of RNAP

            unit.addInteractGroup(new TreeSet<Integer>(Arrays.asList(03, O2)), deltaG_4r - deltaG_14, 0.0f); // haven't found anything in the paper
            unit.addInteractGroup(new TreeSet<Integer>(Arrays.asList(01, O2)), deltaG_4r - deltaG_34, 0.0f); // guessing RNAP int energy to be 0.0

            unit.setRegMolNumber("CAP", (int) Math.round((CAP * 1e-6) * (8.0e-16) * 6.022e23)); //for now try (moles/liter)*(volume of e-coli)*NA
            unit.setRegMolNumber("cAMP", (int) Math.round((cAMP * 1e-6) * (8.0e-16) * 6.022e23));
            return unit.bindingProb();
        }

        @Override
        public int getDimension() {
            return 6;// FIXME: keep in sync with eqns
        }

    }
}
