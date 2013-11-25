/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.ddmmodule;

import fri.cbw.CBWutil.InboundConnectionException;
import fri.cbw.GenericTool.AbstractGenericTool;
import fri.cbw.GenericTool.AbstractParamEvalTool;
import fri.cbw.GenericTool.ToolWrapper;
import fri.cbw.deterministic.tool.AbstractDeterministicModelTool;
import fri.cbw.deterministic.tool.FixedStepHandler;
import fri.cbw.sheekplot.SheekPlotEngine;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.JacobianMatrices;
import org.apache.commons.math3.ode.ParameterizedODE;
import org.apache.commons.math3.ode.nonstiff.EulerIntegrator;
import org.openide.util.Exceptions;
import org.openide.util.lookup.ServiceProvider;

/**
 * Class which implements Direct Differential Method sensitivity analysis of 
 * deterministic biological systems
 * @author miha
 */
@ServiceProvider(service = AbstractParamEvalTool.class)
public class DDMParamsEvalTool extends AbstractParamEvalTool implements SheekPlotEngine {

    private double deltaT=0.1;
    private double simulationTime=100000;
    private int numberOfSamples=500;
        
    transient private boolean resultsAvailable=false;
    transient private String[][] resultNames;
    transient private double[] tSamples;
    transient private double[][][] results;
    
    private String[] xAxisNames;
    private String[] yAxisNames;
    
    transient private boolean connectionsValid=false;;
    transient private int numberOfEquations=0;
    transient private int numberOfParameters=0;
    
    transient private FirstOrderDifferentialEquations ode;
    transient private AbstractDeterministicModelTool deterministicModel;
    
    private Map<String,Double> initialValues;
    
    private Map<String,Double> hY;
    
    private Map<String,Double> hParameters;
    
    private String[] chartTitles;
    
    private boolean normalizeResultsAfterCalculation=true;
    
    /**
     * Updates the engine properties from current model in the tool graph.
     * @param toolWrapper
     * @param scene 
     */
    public void updateFromModel(Object toolWrapper, org.netbeans.api.visual.graph.GraphScene scene){
        ToolWrapper prev = null;
        try {
            prev = ((ToolWrapper)toolWrapper).getPrevNode(scene);
        } catch (ClassCastException ex) {
            Exceptions.printStackTrace(ex);
        } catch (InboundConnectionException ex) {
            Exceptions.printStackTrace(ex);
        }
        AbstractGenericTool prevTool = prev.getNodeGenericTool();
        
        if(!(prevTool instanceof AbstractDeterministicModelTool)) {
            throw new IllegalArgumentException("Input model tool must be Deterministic model");
        } else {
        }
        
        deterministicModel=(AbstractDeterministicModelTool)prevTool;
        
        ode=deterministicModel.getOde();
        
        numberOfEquations=ode.getDimension();
        
        connectionsValid=true;
        
        String[] equationNames=deterministicModel.getEquationNames();
        
        initialValues=updateHashMap(initialValues, equationNames, 0.0);
        
        hY=updateHashMap(hY, equationNames, 0.01);
        
        ParameterizedODE pOde=(ParameterizedODE)ode;
        String[] parameterNames = pOde.getParametersNames().toArray(new String[0]);
        
        double[] defaultHParameterValues=new double[parameterNames.length];
        
        for (int i = 0; i < parameterNames.length; i++) {
            defaultHParameterValues[i]=pOde.getParameter(parameterNames[i])/1000;
            if(defaultHParameterValues[i]==0.0) {
                defaultHParameterValues[i]=0.000001;
            }
        }
        
        hParameters=updateHashMap(hParameters, parameterNames, defaultHParameterValues);
    }
    
    
        
    /**
     * Runs the execution of DDM with current properties.
     * @param toolWrapper
     * @param scene 
     */   
    public void calculate(Object toolWrapper, org.netbeans.api.visual.graph.GraphScene scene) {
        resultsAvailable=false;
        
        //First we update the properties
        this.updateFromModel(toolWrapper, scene);
        
        //we need parameterizedODE for parameter evaluation
        ParameterizedODE pOde=(ParameterizedODE)ode;
        
        //Define the integrator for differential equations
        EulerIntegrator integrator=new EulerIntegrator(deltaT);
        
        //calculate result sample frequency
        int sampleFreq=(int)(simulationTime/(deltaT*(this.numberOfSamples)));
        
        //load the parameters from the model
        String[] parameterNames = pOde.getParametersNames().toArray(new String[0]);
        numberOfParameters=parameterNames.length;
        
        //initialize result arrays
        this.tSamples=new double[numberOfSamples+1];
        int groupsCount = 1+numberOfEquations+numberOfParameters;
        
        this.results=new double[groupsCount][numberOfSamples+1][numberOfEquations];
        this.resultNames=new String[groupsCount][numberOfEquations];
        String[] equationNames = deterministicModel.getEquationNames();
        for (int i = 0; i < groupsCount; i++) {
            this.resultNames[i]=equationNames;
        }
        
        //initialize chart titles
        this.chartTitles=new String[groupsCount];
        this.chartTitles[0]="System chart";
        for (int i = 0 ; i < numberOfEquations; i++) {
            this.chartTitles[i+1]="DDM analysis for variable "+ equationNames[i];
        }
        
        for (int i = 0; i < numberOfParameters; i++) {
            this.chartTitles[i+1+numberOfEquations]="DDM analysis for parameter "+parameterNames[i];
        }
        
        this.xAxisNames=new String[groupsCount];
        this.yAxisNames=new String[groupsCount];
        this.xAxisNames[0]="Time";
        this.yAxisNames[0]="Concentration";
        
        for (int i = 1; i < groupsCount; i++) {
            this.xAxisNames[i]="Time";
            this.yAxisNames[i]="Sensitivity";
        }
        
        FixedStepHandler stepHandler=new FixedStepHandler(this.tSamples, this.results, sampleFreq, true);
        
        integrator.addStepHandler(stepHandler);
        
        double[] y0=new double[numberOfEquations];
        
        //set the initial values of the equations
        for (int i = 0; i < numberOfEquations; i++) {
            y0[i]=this.initialValues.get(equationNames[i]);
        }
                
        double[] hYArray=new double[numberOfEquations];
        
        for (int i = 0; i < hYArray.length; i++) {
            hYArray[i]=this.hY.get(equationNames[i]);
        }
        //initialize jacobian matrices
        JacobianMatrices jacobianMatrices=new JacobianMatrices(ode, hYArray, parameterNames);
        
        //attach jacobianMatrices to the ODE
        jacobianMatrices.setParameterizedODE(pOde);
        
        for (int i = 0; i < parameterNames.length; i++) {
            jacobianMatrices.setParameterStep(parameterNames[i], hParameters.get(parameterNames[i]));
        }
        
        
        ExpandableStatefulODE esOde=new ExpandableStatefulODE(ode);
        esOde.setTime(0);
        esOde.setPrimaryState(y0);
        
        jacobianMatrices.registerVariationalEquations(esOde);
        
        //run the evaluation
        integrator.integrate(esOde, simulationTime);
        
        if(normalizeResultsAfterCalculation){
            this.normalizeResults();
        }
        
        resultsAvailable=true;
        
    }
    
    
    
    @Override
    public String getName() {
        return "Direct Differential Method parameter evaluation tool";
    }

    @Override
    public String getAuthor() {
        return "Miha Nagelj & Marko Janković";
    }

    @Override
    public Class getTopComponentClass() {
        return DDMModuleTopComponent.class;
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
        return xAxisNames;
    }

    /**
     * @return the yAxisName
     */
    @Override
    public String[] getyAxisNames() {
        return yAxisNames;
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

    /**
     * @return the normalizeResults
     */
    public boolean isNormalizeResults() {
        return normalizeResultsAfterCalculation;
    }

    /**
     * @param normalizeResults the normalizeResults to set
     */
    public void setNormalizeResultsAfterCalculation(boolean normalizeResultsAfterCalculation) {
        this.normalizeResultsAfterCalculation = normalizeResultsAfterCalculation;
    }

    private void normalizeResults() {
        ParameterizedODE pOde=(ParameterizedODE)ode;
        String[] parameterNames = pOde.getParametersNames().toArray(new String[0]);
        for (int i = 0; i < parameterNames.length; i++) {
            double parameterValue=pOde.getParameter(parameterNames[i]);
            for (int j = 0; j < numberOfSamples+1; j++) {
                for (int k = 0; k < numberOfEquations; k++) {
                    this.results[1+numberOfEquations+i][j][k]=this.results[1+numberOfEquations+i][j][k]*parameterValue;
                }
            }
        }
    }

        
    private Map<String, Double> updateHashMap(Map<String, Double> map, String[] equationNames, double defaultValue){
        double[] defaultValues=new double[equationNames.length];
        
        for (int i = 0; i < defaultValues.length; i++) {
            defaultValues[i]=defaultValue;
        }
        
        return updateHashMap(map, equationNames, defaultValues);
        
    }
    private Map<String, Double> updateHashMap(Map<String, Double> map, String[] equationNames, double[] defaultValues){
        if(map==null) {
            map=new HashMap<String,Double>(equationNames.length);
        }
        else{
            String[] varNames = map.keySet().toArray(new String[0]);
            for(String varName:varNames){
                boolean found=false;
                for (int i = 0; i < equationNames.length; i++) {
                    if(equationNames[i].equals(varName)){
                        found=true;
                        break;
                    }
                }
                if(!found){
                    map.remove(varName);
                }
            }
        }
        
        for (int i = 0; i < equationNames.length; i++) {
            if(!map.containsKey(equationNames[i])) {
                map.put(equationNames[i],defaultValues[i]);
            }
        }
        return map;
    }

    /**
     * @return the hY
     */
    public Map<String,Double> gethY() {
        return hY;
    }

    /**
     * @param hY the hY to set
     */
    public void sethY(Map<String,Double> hY) {
        this.hY = hY;
    }

    /**
     * @return the hParameters
     */
    public Map<String,Double> gethParameters() {
        return hParameters;
    }

    /**
     * @param hParameters the hParameters to set
     */
    public void sethParameters(Map<String,Double> hParameters) {
        this.hParameters = hParameters;
    }
    
    

}
