/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.ToolPalette;

import java.util.List;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author Sašo
 */
public class ToolTypeNodeFactory extends ChildFactory<String> {

    @Override
    protected boolean createKeys(List<String> list) {
        
        list.add("Model Tools");
        list.add("Param Eval Tools");
        list.add("Plot Tools");
        list.add("Engin Tools");
        
        return true;
    }

    @Override
    protected Node createNodeForKey(String type) {
        Node node = new AbstractNode(Children.create(new ToolNodeFactory(type), true));
        node.setDisplayName(type);
        return node;
        
    }
}
