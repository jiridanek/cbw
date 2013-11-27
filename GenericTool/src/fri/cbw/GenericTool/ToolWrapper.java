/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.GenericTool;

/**
 *
 * @author Sašo
 */

import fri.cbw.CBWutil.InboundConnectionException;
import fri.cbw.GenericTool.AbstractGenericTool;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import org.netbeans.api.visual.graph.GraphScene;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

public class ToolWrapper extends Object implements Transferable, Cloneable, Serializable{
   
   public static final DataFlavor DATA_FLAVOR = new DataFlavor(ToolWrapper.class, "tool");
   
   private String title;
   private String author;
   private AbstractGenericTool nodeGenericTool;
   
   public ToolWrapper(String title, String author, AbstractGenericTool genericTool) {
      this.title  = title;
      this.author = author;
      this.nodeGenericTool = genericTool;
   }
   
   
   public ToolWrapper getPrevNode(GraphScene scene) throws ClassCastException, InboundConnectionException{
       Collection<String> edges = scene.findNodeEdges(this, false, true);
       if(edges != null && !edges.isEmpty()){
           String firstEdge = edges.iterator().next();
           return (ToolWrapper) scene.getEdgeSource(firstEdge);
       }
       
       throw new InboundConnectionException("Modul has no inbound connections");
   }
   
   
   
   public String getTitle() {
      return title;
   }
   
   public String getAuthor() {
      return author;
   }

   @Override
   public DataFlavor[] getTransferDataFlavors() {
      return new DataFlavor[] {DATA_FLAVOR};
   }

   @Override
   public boolean isDataFlavorSupported(DataFlavor flavor) {
      return flavor == DATA_FLAVOR;
   }

   @Override
   public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException,
           IOException {
      if(flavor == DATA_FLAVOR) {
         return this;
      } else {
         throw new UnsupportedFlavorException(flavor);
      }
   }
   
   
   @Override
   public Object clone() throws CloneNotSupportedException{
        return super.clone();   
   }

    /**
     * @return the nodeGenericTool
     */
    public AbstractGenericTool getNodeGenericTool() {
        return nodeGenericTool;
    }

    /**
     * @param nodeGenericTool the nodeGenericTool to set
     */
    public void setNodeGenericTool(AbstractGenericTool nodeGenericTool) {
        this.nodeGenericTool = nodeGenericTool;
    }

}
