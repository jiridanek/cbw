/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.GenericTool;

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
