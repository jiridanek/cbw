/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.MyTestModelTool1;

import fri.cbw.GenericTool.AbstractModelTool;
import fri.cbw.GenericTool.ToolTopComponent;
import org.omg.CORBA.TCKind;
import org.openide.windows.WindowManager;

/**
 *
 * @author Sašo
 */
public class MyTestModelTool1 extends AbstractModelTool {

    private String name = "MyTestModelTool1";
    private String author = "Sašo";
    
    public MyTestModelTool1(){
        
    }
    
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public Class getTopComponentClass(){
        return MyTestModelToolTopComponent.class;
    }
    
}
