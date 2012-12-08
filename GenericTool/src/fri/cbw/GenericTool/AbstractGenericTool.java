/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.GenericTool;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * @author Sašo
 */
public abstract class AbstractGenericTool extends Object implements Serializable{
    private transient ToolTopComponent tc;
    
    abstract public String getName();
    abstract public String getAuthor();
    abstract public Class getTopComponentClass();

    public void openEditor(GraphScene scene, IconNodeWidget node) {
        if(tc == null){
            try {
                tc = (ToolTopComponent) getTopComponentClass().getConstructor(GraphScene.class, IconNodeWidget.class).newInstance(scene, node);
            } catch (InstantiationException ex) {
                Exceptions.printStackTrace(ex);  
            } catch (IllegalAccessException ex) {
                Exceptions.printStackTrace(ex);
            } catch (IllegalArgumentException ex) {
                Exceptions.printStackTrace(ex);
            } catch (InvocationTargetException ex) {
                Exceptions.printStackTrace(ex);
            }catch (NoSuchMethodException ex) {
                Exceptions.printStackTrace(ex);
            } catch (SecurityException ex) {
                Exceptions.printStackTrace(ex);
            }finally{
                if(tc == null){
                    DialogDisplayer.getDefault().notify(
                        new NotifyDescriptor.Message("Prišlo je do napake pri odpiranju maske"));
                    return;
                }
            } 
        }
        tc.open();
        tc.requestActive();
    }

    /**
     * @return the tc
     */
    public ToolTopComponent getTc() {
        return tc;
    }

    /**
     * @param tc the tc to set
     */
    public void setTc(ToolTopComponent tc) {
        this.tc = tc;
    }
}
