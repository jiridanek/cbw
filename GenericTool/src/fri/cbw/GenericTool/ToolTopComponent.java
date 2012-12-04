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
public class ToolTopComponent extends TopComponent{
    private IconNodeWidget toolNode;
    private GraphScene scene;

    /**
     * @param toolNode the toolNode to set
     */
    //public void setToolNode(ToolWraper tool) {
    public void setToolNode(IconNodeWidget tool) {
        toolNode = tool;
    }
    
    /**
     * @return the toolNode
     */
    public IconNodeWidget getToolNode() {
        return toolNode;
    }
    
    void setScene(GraphScene scene) {
        this.scene = scene;
    }

    /**
     * @return the scene
     */
    public GraphScene getScene() {
        return scene;
    }

    
}
