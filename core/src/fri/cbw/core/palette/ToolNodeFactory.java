/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.core.palette;

import fri.cbw.GenericToolInterface.ModelToolInterface;
import fri.cbw.GenericToolInterface.ParamEvalToolInterface;
import java.util.List;
import java.util.Locale;
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
            for(ModelToolInterface mti : Lookup.getDefault().lookupAll(ModelToolInterface.class) ){
                Tool tool = new Tool(mti.getName(), mti.getAuthor(),
                        "com/galileo/netbeans/module/cover_small.jpg",
                        "com/galileo/netbeans/module/cover_big.jpg",
                        mti
                        );
                list.add(tool);
            }
        }
        else if("ParamEvalTool".equals(type)){
            for(ParamEvalToolInterface peti : Lookup.getDefault().lookupAll(ParamEvalToolInterface.class) ){
                Tool tool = new Tool(peti.getName(), peti.getAuthor(),
                        "com/galileo/netbeans/module/cover_small.jpg",
                        "com/galileo/netbeans/module/cover_big.jpg",
                        peti);
                list.add(tool);
            }
        }
        
        
        /*
        Locale[] locales = Locale.getAvailableLocales();
        for(Locale locale : locales){
            if(locale.getDisplayCountry().startsWith(type)){
                Tool tool = new Tool(locale.getDisplayCountry(), "Janez",
                    "com/galileo/netbeans/module/cover_small.jpg",
                    "com/galileo/netbeans/module/cover_big.jpg");
                list.add(tool);
            }
        }
        */
        return true;
    }
    
    @Override
    protected Node createNodeForKey(Tool tool) {
        ToolNode node = new ToolNode(tool);
        
        return node;
        
    }
}