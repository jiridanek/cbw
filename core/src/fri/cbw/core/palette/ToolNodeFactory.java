/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.core.palette;

import fri.cbw.GenericTools.AbstractModelTool;
import fri.cbw.GenericTools.AbstractParamEvalTool;
import fri.cbw.GenericTools.AbstractPlotTool;
import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.Lookup;

/**
 *
 * @author Sašo
 */
class ToolNodeFactory extends ChildFactory<Tool> {

    private String type;
    
    public ToolNodeFactory(String type) {
        this.type = type;
    }

    @Override
    protected boolean createKeys(List<Tool> list) {
        
        if("ModelTool".equals(type)){
            for(AbstractModelTool mti : Lookup.getDefault().lookupAll(AbstractModelTool.class) ){
                Tool tool = new Tool(mti.getName(), mti.getAuthor(), mti);
                list.add(tool);
            }
        }
        else if("ParamEvalTool".equals(type)){
            for(AbstractParamEvalTool peti : Lookup.getDefault().lookupAll(AbstractParamEvalTool.class) ){
                Tool tool = new Tool(peti.getName(), peti.getAuthor(), peti);
                list.add(tool);
            }
        }
        else if("PlotTool".equals(type)){
            for(AbstractPlotTool peti : Lookup.getDefault().lookupAll(AbstractPlotTool.class) ){
                Tool tool = new Tool(peti.getName(), peti.getAuthor(), peti);
                list.add(tool);
            }
        }
        
        return true;
    }
    
    @Override
    protected Node createNodeForKey(Tool tool) {
        ToolNode node = new ToolNode(tool);
        
        return node;
        
    }
}