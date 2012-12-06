/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.DemoModul;
import fri.cbw.GenericTool.AbstractModelTool;
import org.openide.util.lookup.ServiceProvider;
/**
 *
 * @author Sašo
 */
@ServiceProvider(service = AbstractModelTool.class)
public class DemoModulTool extends AbstractModelTool {

    @Override
    public String getName() {
        return "Demo Model Tool";
    }

    @Override
    public String getAuthor() {
        return "Sašo";
    }

    @Override
    public String getTopComponentName() {
        return "DemoModulTopComponent";
    }
    
}
