/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.GenericTool;

import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * @author Sašo
 */
public abstract class AbstractGenericTool extends Object {

    abstract public String getName();

    abstract public String getAuthor();

    abstract public String getTopComponentName();

    public void openEditor(GraphScene scene, IconNodeWidget node) {
        ToolTopComponent tc = (ToolTopComponent) WindowManager.getDefault().findTopComponent(getTopComponentName());
        tc.setToolNode(node);
        tc.setScene(scene);
        tc.open();
        tc.requestActive();
    }
}
