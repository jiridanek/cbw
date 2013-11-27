/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.GenericTool;

import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 *
 * @author Sašo
 */
public abstract class AbstractReaction implements Serializable {
    private LinkedHashMap<AbstractSpecies, Double> products;
    private LinkedHashMap<AbstractSpecies, Double> reactants;

    public AbstractReaction(){
    
    }
    
    
    public void addProduct(AbstractSpecies species, Double quantity){
        if(products == null) {
            products = new LinkedHashMap<AbstractSpecies, Double>();
        }
        products.put(species, quantity);
    }
    
    public void addReactant(AbstractSpecies species, Double quantity){
        if(reactants == null) {
            reactants = new LinkedHashMap<AbstractSpecies, Double>();
        }
        reactants.put(species, quantity);
    }
    
    
    
    /**
     * @return the products
     */
    public LinkedHashMap<AbstractSpecies, Double> getProducts() {
        return products;
    }

    /**
     * @param products the products to set
     */
    public void setProducts(LinkedHashMap<AbstractSpecies, Double> products) {
        this.products = products;
    }

    /**
     * @return the reactants
     */
    public LinkedHashMap<AbstractSpecies, Double> getReactants() {
        return reactants;
    }

    /**
     * @param reactants the reactants to set
     */
    public void setReactants(LinkedHashMap<AbstractSpecies, Double> reactants) {
        this.reactants = reactants;
    }
}
