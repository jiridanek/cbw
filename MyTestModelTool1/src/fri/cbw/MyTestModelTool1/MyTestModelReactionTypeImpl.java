/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.MyTestModelTool1;

import fri.cbw.GenericTool.AbstractReactionType;

/**
 *
 * @author Sašo
 */
public class MyTestModelReactionTypeImpl extends AbstractReactionType{
    private String[] reaction;

    /**
     * @return the reaction
     */
    public String[] getReaction() {
        return reaction;
    }

    /**
     * @param reaction the reaction to set
     */
    public void setReaction(String[] reaction) {
        this.reaction = reaction;
    }
}
