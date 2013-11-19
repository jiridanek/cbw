/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.GenericTool;

import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import org.netbeans.api.visual.graph.GraphScene;

/**
 *
 * @author Sašo
 */
public abstract class AbstractEngineTool extends AbstractGenericTool{
    
    private transient ObservableList<XYChart.Series<Double,Double>>  lineChartData;
    /**
     * @param toolWrapper must be a type of ToolWrapper and is expected to be the EnginTool node
     * @param scene
     * @return 
     */
    abstract public void calculate(Object toolWrapper, GraphScene scene);

    /**
     * @return the lineChartData
     */
    public ObservableList<XYChart.Series<Double,Double>> getLineChartData() {
        return lineChartData;
    }
    

    /**
     * @param lineChartData the lineChartData to set
     */
    public void setLineChartData(ObservableList<XYChart.Series<Double,Double>> lineChartData) {
        this.lineChartData = lineChartData;
    }
    
}
