/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.MyTestModelTool1;

import fri.cbw.GenericTool.AbstractReaction;
import fri.cbw.GenericTool.AbstractSpecies;
import fri.cbw.GenericTool.Unit;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Sašo
 */
public class TestModulReactionImpl extends AbstractReaction{
    private Double kOn;
    private Unit kOnUnit;
    private Double kOff;
    private Unit kOffUnit;
    private boolean reversible;
    
    
    
    public String productsToString() {
        return mapToString(getProducts());
    }
    
    public String reactantsToString() {
        return mapToString(getReactants());
    }
    
    private String mapToString(LinkedHashMap<AbstractSpecies, Double> map) {
        String reactants = "";
        for (Map.Entry<AbstractSpecies, Double> ent : map.entrySet()) {
            reactants += ent.getValue() + " " + ent.getKey().getName() + " + ";
        }
        return reactants.substring(0, reactants.length() - 3);
    }
    
    
    /**
     * @return the kOn
     */
    public Double getkOn() {
        return kOn;
    }

    /**
     * @param kOn the kOn to set
     */
    public void setkOn(Double kOn) {
        this.kOn = kOn;
    }

    /**
     * @return the kOnUnit
     */
    public Unit getkOnUnit() {
        return kOnUnit;
    }

    /**
     * @param kOnUnit the kOnUnit to set
     */
    public void setkOnUnit(Unit kOnUnit) {
        this.kOnUnit = kOnUnit;
    }

    /**
     * @return the kOff
     */
    public Double getkOff() {
        return kOff;
    }

    /**
     * @param kOff the kOff to set
     */
    public void setkOff(Double kOff) {
        this.kOff = kOff;
    }

    /**
     * @return the kOffUnit
     */
    public Unit getkOffUnit() {
        return kOffUnit;
    }

    /**
     * @param kOffUnit the kOffUnit to set
     */
    public void setkOffUnit(Unit kOffUnit) {
        this.kOffUnit = kOffUnit;
    }

    /**
     * @return the reversible
     */
    public boolean isReversible() {
        return reversible;
    }

    /**
     * @param reversible the reversible to set
     */
    public void setReversible(boolean reversible) {
        this.reversible = reversible;
    }
    
}
