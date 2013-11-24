/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.GenericTool;

import fri.cbw.CBWutil.InboundConnectionException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.Exceptions;

/**
 *
 * @author Sašo
 */
public abstract class AbstractGenericTool extends Object implements Serializable{
    private transient ToolTopComponent tc;
    private transient Widget node;
    
    abstract public String getName();
    abstract public String getAuthor();
    abstract public Class getTopComponentClass();

    public void openEditor() {
        
        if(getTc() == null){
            try {
                tc = (ToolTopComponent) getTopComponentClass().getConstructor(AbstractGenericTool.class).newInstance(this);
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
                if(getTc() == null){
                    DialogDisplayer.getDefault().notify(
                        new NotifyDescriptor.Message("Prišlo je do napake pri odpiranju maske"));
                    return;
                }
            } 
        }
        getTc().open();
        getTc().requestActive();
    }
    
    public ToolWrapper getToolWrapper(){
        return (ToolWrapper) getScene().findObject(node);
    }
    
    public AbstractGenericTool getFirstInboundModul() throws InboundConnectionException{
        ToolWrapper tool = getToolWrapper();
        ToolWrapper prevTool = tool.getPrevNode(getScene());
        
        return  prevTool.getNodeGenericTool();
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

    /**
     * @return the scene
     */
    public GraphScene getScene() {
        return (GraphScene) node.getScene();
    }

    /**
     * @return the node
     */
    public Widget getNode() {
        return node;
        
    }

    /**
     * @param node the node to set
     */
    public void setNode(Widget node) {
        this.node = node;
    }
}
