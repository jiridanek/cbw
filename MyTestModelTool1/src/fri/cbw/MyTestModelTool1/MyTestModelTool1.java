/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.MyTestModelTool1;

import fri.cbw.GenericTool.AbstractModelTool;
import fri.cbw.GenericTool.ToolTopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * @author Sašo
 */
public class MyTestModelTool1 extends AbstractModelTool {

    private String name = "MyTestModelTool1";
    private String author = "Sašo";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public String getTopComponentName() {
        return "MyTestModelToolTopComponent";
    }

    @Override
    public String[] getSpecies() {
        MyTestModelToolTopComponent tc = (MyTestModelToolTopComponent) WindowManager.getDefault().findTopComponent(getTopComponentName());

        return tc.getSpeciesList();
    }
    
    @Override
    public String[][] getReactions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
