/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.sheekplot;

/**
 *
 * @author miha
 */
public interface SheekPlotEngine {
    
    /**
     * @return the resultsAvailable
     */
    public boolean getResultsAvailable();
    
    /**
     * @return the results
     */
    public double[][][] getResults();
            

    /**
     * @return the resultNames
     */
    public String[][] getResultNames();

    /**
     * @return the tSamples
     */
    public double[] gettSamples();

    /**
     * @return the xAxisName
     */
    public String[] getxAxisNames();

    /**
     * @return the yAxisName
     */
    public String[] getyAxisNames();
    
    /**
     * @return the chartTitles
     */
    public String[] getChartTitles();
}
