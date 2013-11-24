/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.CBWutil;

/**
 *
 * @author Sašo
 */
public class CBWUtil {
    
    static public Double getDoubleNull(String d){
        if (d == null || d.trim().isEmpty())
            return null;
        try{
            return Double.parseDouble(d);
        }catch(Exception e){
            return null;
        }
            
    }
    
    static public Double getDoubleZero(String d){
        Double ret = getDoubleNull(d);
        return (ret == null)? 0d : ret;
    }
}
