/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.sheekplot;

import fri.cbw.GenericTool.AbstractPlotTool;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author miha
 */
@ServiceProvider(service = AbstractPlotTool.class)
public class SheekPlotTool extends AbstractPlotTool {
    
    private String name="Sheek plot tool";
    @Override
    public String getName() {
        return name;
    }
    
    private String author="Miha Nagelj & Marko Janković";
    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public Class getTopComponentClass() {
        return SheekPlotToolTopComponent.class;
    }
    
}
