/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.deterministic.repressilator;


import fri.cbw.GenericTool.AbstractModelTool;
import fri.cbw.deterministic.tool.AbstractDeterministicModelTool;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.openide.util.lookup.ServiceProvider;

/**
 * Class that represent deterministic repressilator model
 * @author miha
 */
@ServiceProvider(service = AbstractModelTool.class)
public class RepressilatorModelTool extends AbstractDeterministicModelTool {
    
    private String[] equationNames=new String[]{"px", "py", "pz","mx", "my", "mz"};
    
    private RepressilatorODE ode;
    
    /**
     * Returns the differential equations system
     * @return 
     */
    @Override
    public FirstOrderDifferentialEquations getOde() {
        if(ode==null) {
            ode=new RepressilatorODE();
        }
        return ode;
    }
    
    private String name="Deterministic repressilator model";
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String[] getEquationNames() {
        return this.equationNames;
    }
    
}
