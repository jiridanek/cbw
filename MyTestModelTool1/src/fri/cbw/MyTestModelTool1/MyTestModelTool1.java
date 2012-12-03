/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.MyTestModelTool1;

import fri.cbw.GenericTool.AbstractModelTool;

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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String[] getReactions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

  
    
}
