/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.MyTestParamEvalTool1;

import fri.cbw.GenericToolInterface.ParamEvalToolInterface;

/**
 *
 * @author Sašo
 */
public class MyTestParamEvalTool1 extends ParamEvalToolInterface{

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
