/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.MyTestModelTool1;

import fri.cbw.GenericToolInterface.ModelToolInterface;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * @author Sašo
 */
public class MyTestModelTool1 extends ModelToolInterface {
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

  
    
}
