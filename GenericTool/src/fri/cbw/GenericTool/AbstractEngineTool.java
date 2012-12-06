/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.GenericTool;

import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import org.netbeans.api.visual.graph.GraphScene;

/**
 *
 * @author Sašo
 */
public abstract class AbstractEngineTool extends AbstractGenericTool{
    
    
    /**
     * @param toolWrapper must be a type of ToolWrapper and is expected to be the EnginTool node
     * @param scene
     * @return 
     */
    abstract public ObservableList<XYChart.Series<Double,Double>> getLineChartData(Object toolWrapper, GraphScene scene);
    
}
