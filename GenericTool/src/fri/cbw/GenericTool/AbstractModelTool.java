/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.GenericTool;

import java.util.List;

/**
 *
 * @author Sašo
 */
public abstract class AbstractModelTool extends AbstractGenericTool {
    
    private List<String> species;
    private List<AbstractReactionType> reactions;

    /**
     * @return the species
     */
    public List<String> getSpecies() {
        return species;
    }

    /**
     * @param species the species to set
     */
    public void setSpecies(List<String> species) {
        this.species = species;
    }

    /**
     * @return the reactions
     */
    public List<AbstractReactionType> getReactions() {
        return reactions;
    }

    /**
     * @param reactions the reactions to set
     */
    public void setReactions(List<AbstractReactionType> reactions) {
        this.reactions = reactions;
    }
    
}
