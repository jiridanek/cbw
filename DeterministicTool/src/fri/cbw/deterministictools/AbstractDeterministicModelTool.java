/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.deterministictools;


/**
 * Class for representing deterministic biological system presented with first order differential equations
 * @author miha
 */
public abstract class AbstractDeterministicModelTool extends AbstractModelTool {
    /**
     * @return the first order differential equation system
     */
    public abstract FirstOrderDifferentialEquations getOde();
    
    /**
     * 
     * @return the differential equation names
     */
    public abstract String[] getEquationNames();
}
