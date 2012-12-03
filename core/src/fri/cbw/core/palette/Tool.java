/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.core.palette;

/**
 *
 * @author Sašo
 */

import fri.cbw.GenericTool.AbstractGenericTool;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class Tool extends Object implements Transferable, Cloneable{
   
   public static final DataFlavor DATA_FLAVOR = new DataFlavor(Tool.class, "tool");
   
   private String title;
   private String author;
   private AbstractGenericTool gt;
   
   public Tool(String title, String author, AbstractGenericTool gt) {
      this.title  = title;
      this.author = author;
      this.gt = gt;
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
     * @return the gt
     */
    public AbstractGenericTool getGt() {
        return gt;
    }

    /**
     * @param gt the gt to set
     */
    public void setGt(AbstractGenericTool gt) {
        this.gt = gt;
    }

}
