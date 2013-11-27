/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.GenericTool;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author Sašo
 */
public abstract class AbstractModelTool extends AbstractGenericTool {
    
    private LinkedHashMap<AbstractSpecies, Double> species;
    private ArrayList<AbstractReaction> reactions;
    
    
    public void addSpecies(String speciesName, Double quantity){
        if(species == null) {
            species = new LinkedHashMap<AbstractSpecies, Double>();
        }
        species.put(new AbstractSpecies(speciesName), quantity);
    }
    
    public void addSpecies(String speciesName){
        addSpecies(speciesName, 0d);
    }
    
    
    /**
     * @return the species
     */
    public LinkedHashMap<AbstractSpecies, Double> getSpecies() {
        return species;
    }

    /**
     * @param species the species to set
     */
    public void setSpecies(LinkedHashMap<AbstractSpecies, Double> species) {
        this.species = species;
    }

    /**
     * @return the reactions
     */
    public List<AbstractReaction> getReactions() {
        return reactions;
    }

    /**
     * @param reactions the reactions to set
     */
    public void setReactions(ArrayList<AbstractReaction> reactions) {
        this.reactions = reactions;
    }
    
}
