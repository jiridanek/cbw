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
    
    private String savedText;
    
    @Override
    public String getName() {
        return "Demo Model Tool";
    }

    @Override
    public String getAuthor() {
        return "Sašo";
    }

    @Override
    public Class getTopComponentClass() {
        return DemoModulTopComponent.class;
    }

    /**
     * @return the savedText
     */
    public String getSavedText() {
        return savedText;
    }

    /**
     * @param savedText the savedText to set
     */
    public void setSavedText(String savedText) {
        this.savedText = savedText;
    }

    
}
