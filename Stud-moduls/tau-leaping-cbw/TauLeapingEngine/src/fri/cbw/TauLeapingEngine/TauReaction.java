package fri.cbw.TauLeapingEngine;

import fri.cbw.GenericTool.AbstractReactionType;

/**
 *
 * @author Boštjan Cigan
 * Data input example:
 * R1 + R2 => R3 
 * R3 + R2 => R1
 * R4 => R3
 * R3 => R6
 * N = 6, M = 4 int, int
 * v0 = [-1,-1,1]; 2D array of ints
 * v1 = [-1,-1,1];
 * v2 = [-1,1];
 * v3 = [-1,1];
 * RS0 = [0,1,2]; 2D array of ints
 * RS1 = [2,1,0];
 * RS2 = [3,2];
 * RS3 = [2,5];
 * 
 * + k parameter za vsako reakcijo (če je dvosmerna sta dva)
 * Če je reakcija dvosmerna se to pretvori v dve vrstici v tabeli
 *
 */
public class TauReaction extends AbstractReactionType {
    
    private long[][] v; // vectors
    private long[][] indexes; // indexes of reactions
    private long n; // number of species
    private long m; // number of reactions
    private long omega; // omega parameter
    private double time; // time limit
    private double[] k; // speed of reaction
    private long[] x0;

    public long[] getX0() {
        return x0;
    }
    
    public void setX0(long[] x0) {
        this.x0 = x0;
    }
    
    public long[][] getV() {
        return v;
    }

    public void setV(long[][] v) {
        this.v = v;
    }

    public long[][] getIndexes() {
        return indexes;
    }

    public void setIndexes(long[][] indexes) {
        this.indexes = indexes;
    }

    public long getN() {
        return n;
    }

    public void setN(long n) {
        this.n = n;
    }

    public long getM() {
        return m;
    }

    public void setM(long m) {
        this.m = m;
    }

    public long getOmega() {
        return omega;
    }

    public void setOmega(long omega) {
        this.omega = omega;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double[] getK() {
        return k;
    }

    public void setK(double[] k) {
        this.k = k;
    }
    
}
