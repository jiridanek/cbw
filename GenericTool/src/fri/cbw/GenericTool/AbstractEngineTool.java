/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.GenericTool;

import javafx.scene.chart.XYChart;
import org.netbeans.api.visual.graph.GraphScene;

/**
 *
 * @author Sašo
 */
public abstract class AbstractEngineTool extends AbstractGenericTool{
    
    private transient XYChart chartData;
    /**
     * @param toolWrapper must be a type of ToolWrapper and is expected to be the EnginTool node
     * @param scene
     * @return 
     */
    abstract public void calculate();

    /**
     * @return the lineChartData
     */
    public XYChart getLineChartData() {
        return chartData;
    }
    
    /**
     * @param lineChartData the lineChartData to set
     */
    public void setLineChartData(XYChart chartData) {
        this.chartData = chartData;
    }
    
}
