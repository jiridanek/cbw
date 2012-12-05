/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.GenericTool;

/**
 *
 * @author Sašo
 */
public abstract class AbstractModelTool extends AbstractGenericTool {
    
    private String[] species;
    private String[][] reactions;

    /**
     * @return the species
     */
    public String[] getSpecies() {
        return species;
    }

    /**
     * @param species the species to set
     */
    public void setSpecies(String[] species) {
        this.species = species;
    }

    /**
     * @return the reactions
     */
    public String[][] getReactions() {
        return reactions;
    }

    /**
     * @param reactions the reactions to set
     */
    public void setReactions(String[][] reactions) {
        this.reactions = reactions;
    }
    
}
