/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.GenericTool;

import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * @author Sašo
 */
public abstract class AbstractGenericTool extends Object {
    
    abstract public String getName();
    
    abstract public String getAuthor();
    
     public void openEditor(){
        TopComponent tc = WindowManager.getDefault().findTopComponent(getTopComponentName());
        tc.open();
        tc.requestActive();
     }
    
    abstract public String getTopComponentName();
    
}
