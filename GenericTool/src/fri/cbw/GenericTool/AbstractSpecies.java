/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.GenericTool;

import java.io.Serializable;

/**
 *
 * @author Sašo
 */
public class AbstractSpecies implements Serializable {
    private String name;
    private Unit unit;
    
    public AbstractSpecies(String name, Unit unit){
        this.name = name;
        this.unit = unit;
    }
    
    public AbstractSpecies(String name){
        this.name = name;
        
    }
    
    @Override
    public int hashCode(){
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AbstractSpecies other = (AbstractSpecies) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equalsIgnoreCase(other.name)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString(){
        return name;
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the unit
     */
    public Unit getUnit() {
        return unit;
    }

    /**
     * @param unit the unit to set
     */
    public void setUnit(Unit unit) {
        this.unit = unit;
    }
}
