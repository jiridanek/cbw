/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.GenericTool;

/**
 *
 * @author Sašo
 */
public enum Unit {

    GRAM("Gram", "g", 1.0d),
    MOL("Mol", "mol", 1.0d);
    
    private String label;
    private String symbol;
    private Double conversion;
    
    
    private Unit(String label, String symbol, Double conversion) {
        this.label = label;
        this.symbol = symbol;
        this.conversion = conversion;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @return the symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * @return the conversion
     */
    public Double getConversion() {
        return conversion;
    }
    
}
