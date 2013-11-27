/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.deterministic.tool;

import fri.cbw.CBWutil.InboundConnectionException;
import fri.cbw.GenericTool.AbstractEngineTool;
import fri.cbw.GenericTool.AbstractGenericTool;
import fri.cbw.GenericTool.ToolWrapper;
import fri.cbw.sheekplot.SheekPlotEngine;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.nonstiff.EulerIntegrator;
import org.openide.util.lookup.ServiceProvider;

/**
 * Class for representing engine for running deterministic biological systems
 * which are presented with first order differential equations.
 * @author miha
 */
@ServiceProvider(service = AbstractEngineTool.class)
public class EulerDeterministicEngine extends AbstractEngineTool implements SheekPlotEngine {
    
    private double deltaT=0.1;
    private double simulationTime=100000;
    
    //No of samples to capture during execution
    private int numberOfSamples=500;
        
    transient private boolean resultsAvailable=false;
    transient private String[][] resultNames;
    transient private double[] tSamples;
    transient private double[][][] results;
    
    private String[] xAxisName=new String[]{"Time"};
    private String[] yAxisName=new String[]{"Concentration"};
    
    transient private boolean connectionsValid=false;;
    transient private int numberOfEquations=0;
    
    transient private FirstOrderDifferentialEquations ode;
    transient private AbstractDeterministicModelTool deterministicModel;
    
    private Map<String,Double> initialValues;
    private String[] chartTitles=new String[]{"Repressilator"};
    
    /**
     * Updates the engine properties from current model in the tool graph.
     * @param toolWrapper
     * @param scene 
     */
    public void updateFromModel() {
        
        AbstractGenericTool prevTool = null;
        try {
            prevTool = getFirstInboundModul();
        } catch (InboundConnectionException ex) {
            Logger.getLogger(EulerDeterministicEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(!(prevTool instanceof AbstractDeterministicModelTool)) {
            throw new IllegalArgumentException("Input model tool must be Deterministic model");
        }
        deterministicModel=(AbstractDeterministicModelTool)prevTool;
        
        ode=deterministicModel.getOde();
        
        numberOfEquations=ode.getDimension();
        
        connectionsValid=true;
        
        String[] equationNames=deterministicModel.getEquationNames();
        
        if(initialValues==null) {
            initialValues=new HashMap<String,Double>(equationNames.length);
        }
        else{
            String[] varNames = initialValues.keySet().toArray(new String[0]);
            for(String varName:varNames){
                boolean found=false;
                for (int i = 0; i < equationNames.length; i++) {
                    if(equationNames[i].equals(varName)){
                        found=true;
                        break;
                    }
                }
                if(!found){
                    initialValues.remove(varName);
                }
            }
        }
        
        for (int i = 0; i < equationNames.length; i++) {
            if(!initialValues.containsKey(equationNames[i])) {
                initialValues.put(equationNames[i],0.0);
            }
        }
                
    }
    
    
    /**
     * Runs the execution of engine with current properties.
     * @param toolWrapper
     * @param scene 
     */    
    @Override
    public void calculate() {
        resultsAvailable=false;
        //First we update the properties
        this.updateFromModel();
        
        //Define the integrator for differential equations
        EulerIntegrator integrator=new EulerIntegrator(deltaT);
        
        //calculate result sample frequency
        int sampleFreq=(int)(simulationTime/(deltaT*(this.numberOfSamples)));
        
        //initialize result arrays
        this.tSamples=new double[numberOfSamples+1];
        this.results=new double[1][numberOfSamples+1][numberOfEquations];
        this.resultNames=new String[][]{deterministicModel.getEquationNames()};
        
        //initialize step handler for logging the state during execution
        FixedStepHandler stepHandler=new FixedStepHandler(this.tSamples, this.results, sampleFreq);
        integrator.addStepHandler(stepHandler);
        
        double[] y0=new double[numberOfEquations];
        
        String[] equationNames=deterministicModel.getEquationNames();
        //set the initial values of the equations
        for (int i = 0; i < numberOfEquations; i++) {
            y0[i]=this.initialValues.get(equationNames[i]);
        }
        double[] y=new double[numberOfEquations];
        //run the differential equation system
        integrator.integrate(ode, 0, y0, simulationTime, y);
        
        resultsAvailable=true;
        
    }
    
    private String name="Deterministic system engine";
    @Override
    public String getName() {
        return name;
    }

    private String author="Miha Nagelj & Marko Janković";
    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public Class getTopComponentClass() {
        return EulerDeterministicEngineTopComponent.class;
    }
    
    @Override
    public boolean getResultsAvailable() {
        return resultsAvailable;
    }

    /**
     * @return the results
     */
    @Override
    public double[][][] getResults() {
        return results;
    }

    /**
     * @return the resultNames
     */
    @Override
    public String[][] getResultNames() {
        return resultNames;
    }

    /**
     * @return the tSamples
     */
    @Override
    public double[] gettSamples() {
        return tSamples;
    }

    /**
     * @return the xAxisName
     */
    @Override
    public String[] getxAxisNames() {
        return xAxisName;
    }

    /**
     * @return the yAxisName
     */
    @Override
    public String[] getyAxisNames() {
        return yAxisName;
    }

    /**
     * @return the deltaT
     */
    public double getDeltaT() {
        return deltaT;
    }

    /**
     * @param deltaT the deltaT to set
     */
    public void setDeltaT(double deltaT) {
        this.deltaT = deltaT;
    }

    /**
     * @return the simulationTime
     */
    public double getSimulationTime() {
        return simulationTime;
    }

    /**
     * @param simulationTime the simulationTime to set
     */
    public void setSimulationTime(double simulationTime) {
        this.simulationTime = simulationTime;
    }

    /**
     * @return the connectionsValid
     */
    public boolean isConnectionsValid() {
        return connectionsValid;
    }

    /**
     * @return the numberOfEquations
     */
    public int getNumberOfEquations() {
        return numberOfEquations;
    }

    /**
     * @return the ode
     */
    public FirstOrderDifferentialEquations getOde() {
        return ode;
    }

    /**
     * @return the initialValues
     */
    public Map<String,Double> getInitialValues() {
        return initialValues;
    }

    /**
     * @param initialValues the initialValues to set
     */
    public void setInitialValues(Map<String,Double> initialValues) {
        this.initialValues = initialValues;
    }

    /**
     * @return the numberOfSamples
     */
    public int getNumberOfSamples() {
        return numberOfSamples;
    }

    /**
     * @param numberOfSamples the numberOfSamples to set
     */
    public void setNumberOfSamples(int numberOfSamples) {
        this.numberOfSamples = numberOfSamples;
    }

    @Override
    public String[] getChartTitles() {
        return this.chartTitles;
    }
    
}
