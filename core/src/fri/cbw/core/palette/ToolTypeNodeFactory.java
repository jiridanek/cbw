/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.core.palette;

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
        
        /*
         * for(char letter = 'A'; letter <= 'Z'; letter++){
            list.add(String.valueOf(letter));
        }*/
        
        list.add("ModelTool");
        list.add("ParamEvalTool");
        
        return true;
    }

    @Override
    protected Node createNodeForKey(String type) {
        Node node = new AbstractNode(Children.create(new ToolNodeFactory(type), true));
        node.setDisplayName(type);
        return node;
        
    }
}
