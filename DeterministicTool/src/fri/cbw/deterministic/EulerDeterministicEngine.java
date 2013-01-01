/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.deterministic;

import fri.cbw.GenericTool.AbstractEngineTool;
import fri.cbw.GenericTool.AbstractGenericTool;
import fri.cbw.ToolGraph.ToolWrapper;
import fri.cbw.sheekplot.SheekPlotEngine;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.nonstiff.EulerIntegrator;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author miha
 */
@ServiceProvider(service = AbstractEngineTool.class)
public class EulerDeterministicEngine extends AbstractEngineTool implements SheekPlotEngine {
    
    private double deltaT=0.1;
    private double simulationTime=100000;
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
    
    public void updateFromModel(Object toolWrapper, org.netbeans.api.visual.graph.GraphScene scene){
        ToolWrapper prev = ((ToolWrapper)toolWrapper).getPrevNode(scene);
        AbstractGenericTool prevTool = prev.getNodeGenericTool();
        
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
    
    
        
    @Override
    public void calculate(Object toolWrapper, org.netbeans.api.visual.graph.GraphScene scene) {
        resultsAvailable=false;
        this.updateFromModel(toolWrapper, scene);
        
        EulerIntegrator integrator=new EulerIntegrator(deltaT);
        
        int sampleFreq=(int)(simulationTime/(deltaT*(this.numberOfSamples)));
        
        this.tSamples=new double[numberOfSamples+1];
        this.results=new double[1][numberOfSamples+1][numberOfEquations];
        this.resultNames=new String[][]{deterministicModel.getEquationNames()};
        
        FixedStepHandler stepHandler=new FixedStepHandler(this.tSamples, this.results, sampleFreq);
        
        integrator.addStepHandler(stepHandler);
        
        double[] y0=new double[numberOfEquations];
        
        String[] equationNames=deterministicModel.getEquationNames();
        
        for (int i = 0; i < numberOfEquations; i++) {
            y0[i]=this.initialValues.get(equationNames[i]);
        }
        double[] y=new double[numberOfEquations];
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
