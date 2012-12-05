/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.ToolPalette;

import fri.cbw.GenericTool.AbstractEnginTool;
import fri.cbw.ToolGraph.ToolWrapper;
import fri.cbw.GenericTool.AbstractModelTool;
import fri.cbw.GenericTool.AbstractParamEvalTool;
import fri.cbw.GenericTool.AbstractPlotTool;
import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.Lookup;

/**
 *
 * @author Sašo
 */
class ToolNodeFactory extends ChildFactory<ToolWrapper> {

    private String type;
    
    public ToolNodeFactory(String type) {
        this.type = type;
    }

    @Override
    protected boolean createKeys(List<ToolWrapper> list) {
        
        if("Model Tools".equals(type)){
            for(AbstractModelTool at : Lookup.getDefault().lookupAll(AbstractModelTool.class) ){
                ToolWrapper tool = new ToolWrapper(at.getName(), at.getAuthor(), at);
                list.add(tool);
            }
        }
        else if("Param Eval Tools".equals(type)){
            for(AbstractParamEvalTool at : Lookup.getDefault().lookupAll(AbstractParamEvalTool.class) ){
                ToolWrapper tool = new ToolWrapper(at.getName(), at.getAuthor(), at);
                list.add(tool);
            }
        }
        else if("Plot Tools".equals(type)){
            for(AbstractPlotTool at : Lookup.getDefault().lookupAll(AbstractPlotTool.class) ){
                ToolWrapper tool = new ToolWrapper(at.getName(), at.getAuthor(), at);
                list.add(tool);
            }
        }
        else if("Engin Tools".equals(type)){
            for(AbstractEnginTool at : Lookup.getDefault().lookupAll(AbstractEnginTool.class) ){
                ToolWrapper tool = new ToolWrapper(at.getName(), at.getAuthor(), at);
                list.add(tool);
            }
        }
        
        return true;
    }
    
    @Override
    protected Node createNodeForKey(ToolWrapper tool) {
        ToolNode node = new ToolNode(tool);
        
        return node;
        
    }
}