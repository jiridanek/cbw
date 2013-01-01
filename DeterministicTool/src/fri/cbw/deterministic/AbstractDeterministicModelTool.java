/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.deterministic;

import fri.cbw.GenericTool.AbstractModelTool;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;

/**
 *
 * @author miha
 */
public abstract class AbstractDeterministicModelTool extends AbstractModelTool {
    /**
     * @return the ode
     */
    public abstract FirstOrderDifferentialEquations getOde();
    
    
    public abstract String[] getEquationNames();
}
