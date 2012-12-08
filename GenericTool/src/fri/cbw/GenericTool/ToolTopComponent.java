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
    private IconNodeWidget toolNode;
    private GraphScene scene;
    
    public ToolTopComponent(GraphScene scene, IconNodeWidget toolNode){
        this.scene = scene;
        this.toolNode = toolNode;
    }
    /**
     * This method is called just before saving to file is executed
     */
    public void doSave(){  }
    
    public Object getToolWrapper(){
        return getScene().findObject (getToolNode());
    }
    
    public AbstractGenericTool getGenericTool(){
        java.lang.reflect.Method method = null;
        Object tw = getToolWrapper();

        try {
            method = tw.getClass().getMethod("getNodeGenericTool");
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        try {
            return (AbstractGenericTool) method.invoke(tw);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    public void setToolNode(IconNodeWidget tool) {
        toolNode = tool;
    }
   
    public IconNodeWidget getToolNode() {
        return toolNode;
    }
    
    public void setScene(GraphScene scene) {
        this.scene = scene;
    }

    public GraphScene getScene() {
        return scene;
    }
}
