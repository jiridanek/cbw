/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.GenericTool;

import java.lang.reflect.InvocationTargetException;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.openide.windows.TopComponent;

/**
 *
 * @author Sašo
 */
public abstract class ToolTopComponent extends TopComponent{
    private AbstractGenericTool genericTool;
    
    public ToolTopComponent(AbstractGenericTool genericTool){
        this.genericTool = genericTool;
    }
    /**
     * This method is called just before saving to file is executed
     */
    abstract public void doSave();
    
    public Object getToolWrapper(){
        return getGenericTool().getToolWrapper();
    }

    /**
     * @return the genericTool
     */
    public AbstractGenericTool getGenericTool() {
        return genericTool;
    }
}
