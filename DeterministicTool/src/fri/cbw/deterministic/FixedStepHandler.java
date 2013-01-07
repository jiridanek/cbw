/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.deterministic;

import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;

/**
 *  Class for logging the deterministic system state during execution
 * @author miha
 */
public class FixedStepHandler implements StepHandler{
    private double[] tSamples;
    private double[][][] results;
    
    private int currentIndex=0;
    private int currentIteration=0;
    private int sampleFreq=0;
    private boolean logSecondaryState=false;
    
    private int groupCount;
    
    /**
     * 
     * @param tSamples Array for logging time samples
     * @param results Array for logging the results
     * @param sampleFreq Frequency of sampling the data (every x sample)
     */
    public FixedStepHandler(double[] tSamples, double[][][] results, int sampleFreq){
        this(tSamples, results, sampleFreq, false);
    }
    
    /**
     * 
     * @param tSamples Array for logging time samples
     * @param results Array for logging the results
     * @param sampleFreq Frequency of sampling the data (every x sample)
     * @param logSecondaryState Log secondary state used in advanced executions (DDM)
     */
    public FixedStepHandler(double[] tSamples, double[][][] results, int sampleFreq, boolean logSecondaryState){
        this.tSamples=tSamples;
        this.results=results;
        this.sampleFreq=sampleFreq;
        this.logSecondaryState=logSecondaryState;
        
        this.groupCount=results.length;
        
    }
    
    @Override
    public void init(double t0, double[] y0, double t) {
        this.currentIndex=0;
        this.currentIteration=0;
        this.tSamples[this.currentIndex]=t0;
        int start=0;
        for (int i = 0; i < groupCount; i++) {
            System.arraycopy(y0,start,this.results[i][this.currentIndex],0,this.results[i][this.currentIndex].length);
            start+=this.results[i][this.currentIndex].length;
        }
        
        this.currentIndex++;
        this.currentIteration++;
    }
            
    @Override
    public void handleStep(StepInterpolator interpolator, boolean isLast) {
        if(this.currentIteration % this.sampleFreq == 0){
            double   t = interpolator.getCurrentTime();
            double[] y = interpolator.getInterpolatedState();
            
            this.tSamples[this.currentIndex]=t;
            System.arraycopy(y, 0, this.results[0][this.currentIndex], 0, y.length);
            
            if(this.logSecondaryState){
                double[] secondaryState=interpolator.getInterpolatedSecondaryState(0);
                int start=0;
                for (int i = 1; i < groupCount; i++) {
                    System.arraycopy(secondaryState, start, this.results[i][this.currentIndex], 0, this.results[i][this.currentIndex].length);
                    start+=this.results[i][this.currentIndex].length;
                }
            }
            
            this.currentIndex++;
        }
        this.currentIteration++;
        
    }
}
