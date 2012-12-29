/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.ddmmodule;

import fri.cbw.GenericTool.AbstractParamEvalTool;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author miha
 */
@ServiceProvider(service = AbstractParamEvalTool.class)
public class DDMParamsEvalTool extends AbstractParamEvalTool {

    @Override
    public String getName() {
        return "Direct Differential Method parameter evaluation tool";
    }

    @Override
    public String getAuthor() {
        return "Miha Nagelj & Marko Janković";
    }

    @Override
    public Class getTopComponentClass() {
        return DDMModuleTopComponent.class;
    }
}
