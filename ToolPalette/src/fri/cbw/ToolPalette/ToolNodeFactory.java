/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.ToolPalette;

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
        
        if("ModelTool".equals(type)){
            for(AbstractModelTool mti : Lookup.getDefault().lookupAll(AbstractModelTool.class) ){
                ToolWrapper tool = new ToolWrapper(mti.getName(), mti.getAuthor(), mti);
                list.add(tool);
            }
        }
        else if("ParamEvalTool".equals(type)){
            for(AbstractParamEvalTool peti : Lookup.getDefault().lookupAll(AbstractParamEvalTool.class) ){
                ToolWrapper tool = new ToolWrapper(peti.getName(), peti.getAuthor(), peti);
                list.add(tool);
            }
        }
        else if("PlotTool".equals(type)){
            for(AbstractPlotTool peti : Lookup.getDefault().lookupAll(AbstractPlotTool.class) ){
                ToolWrapper tool = new ToolWrapper(peti.getName(), peti.getAuthor(), peti);
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