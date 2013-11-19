/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.BooleanModul;
import fri.cbw.GenericTool.AbstractModelTool;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author TadejJ
 */

@ServiceProvider(service = AbstractModelTool.class)
public class BooleanModelTool extends AbstractModelTool{
    
    public BooleanModelTool(){}
    
    @Override
    public String getName() {
        return "Boolean Model Tool";
    }

    @Override
    public String getAuthor() {
       return "Tadej, Ziga, Mirko";
    }

    @Override
    public Class getTopComponentClass() {
        return BooleanModelToolTopComponent.class;
    }
}