/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.core.palette;

/**
 *
 * @author Sašo
 */

import fri.cbw.GenericToolInterface.GenericToolInterface;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.beans.BeanInfo;
import java.io.IOException;
import org.netbeans.api.visual.action.WidgetAction;
import org.openide.util.ImageUtilities;

public class Tool extends Object implements Transferable, Cloneable{
   
   public static final DataFlavor DATA_FLAVOR = new DataFlavor(Tool.class, "tool");
   
   private String title;
   private String author;
   private GenericToolInterface gt;
   
   public Tool(String title, String author, String icon16, String icon32, GenericToolInterface gt) {
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
    public GenericToolInterface getGt() {
        return gt;
    }

    /**
     * @param gt the gt to set
     */
    public void setGt(GenericToolInterface gt) {
        this.gt = gt;
    }

}
