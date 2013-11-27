/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.MyTestPlotTool1;

import fri.cbw.GenericTool.AbstractPlotTool;
import fri.cbw.GenericTool.ToolTopComponent;

/**
 *
 * @author Sašo
 */
public class MyTestPlotTool extends AbstractPlotTool{

    @Override
    public String getName() {
        return "MyTestPlotTool";
    }

    @Override
    public String getAuthor() {
        return "Sašo";
    }

    @Override
    public Class getTopComponentClass() {
        return MyTestPlotToolTopComponent.class;
    }
    
}
