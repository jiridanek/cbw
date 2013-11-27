/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.BooleanModul;

import fri.cbw.GenericTool.AbstractReaction;
import java.io.Serializable;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author TadejJ
 */
public class BooleanModelFunctionsTypeImpl extends AbstractReaction implements Serializable{
    
    private String booleanFunctions;
    
    BooleanModelFunctionsTypeImpl(String booleanFunctions){
        this.booleanFunctions = booleanFunctions;
    }
    
    /**
     * @return the booleanFunction
     */
    public String getBooleanFunctions() {
        return booleanFunctions;
    }

    /**
     * @param booleanFunction the booleanFunction to set
     */
    public void setBooleanFunctions(String booleanFunctions) {
        this.booleanFunctions = booleanFunctions;
        
    }
}
