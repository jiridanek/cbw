/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.MyTestParamEvalTool1;

import fri.cbw.GenericTools.AbstractParamEvalTool;

/**
 *
 * @author Sašo
 */
public class MyTestParamEvalTool1 extends AbstractParamEvalTool{

    @Override
    public String getName() {
        return "MyTestParamEvalTool1";
    }

    @Override
    public String getAuthor() {
        return "Janez";
    }

    @Override
    public String getTopComponentName() {
        return "MyTestParamEvalToolTopComponent";
    }
    
}
