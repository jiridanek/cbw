/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.MyTestEnginTool1;

import fri.cbw.GenericTool.AbstractEngineTool;
import fri.cbw.GenericTool.AbstractModelTool;
import fri.cbw.ToolGraph.ToolWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import org.netbeans.api.visual.graph.GraphScene;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Sašo
 */

@ServiceProvider(service = AbstractEngineTool.class)
public class MyTestEnginTool1 extends AbstractEngineTool{
    
    @Override
    public void calculate(Object toolWrapper, GraphScene scene) {
        calculateData(toolWrapper, scene);
    }
    
    @Override
    public ObservableList<XYChart.Series<Double,Double>> getLineChartData() {
        return getLineChartData();
    }
    
    @Override
    public String getName() {
        return "MyTestEnginTool1";
    }

    @Override
    public String getAuthor() {
        return "Sašo";
    }

    @Override
    public Class getTopComponentClass() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    

    private void calculateData(Object tool, GraphScene scene) {
        ToolWrapper prev = ((ToolWrapper)tool).getPrevNode(scene);
        AbstractModelTool mt = (AbstractModelTool) prev.getNodeGenericTool();
        
        String [] species = mt.getSpecies();
        
        LineChart.Series<Double,Double> [] es = new LineChart.Series[species.length];
        
        for (int i = 0; i < species.length; i++) {
            es[i] = new LineChart.Series<Double,Double>(species[i], FXCollections.observableArrayList(
               new XYChart.Data<Double,Double>(0.0, 1.0 + i),
               new XYChart.Data<Double,Double>(1.2, 1.4 + i),
               new XYChart.Data<Double,Double>(2.2, 1.9 + i),
               new XYChart.Data<Double,Double>(2.7, 2.3 + i),
               new XYChart.Data<Double,Double>(2.9, 0.5 + i)
           ));
        }
        
        setLineChartData(FXCollections.observableArrayList(es));
    }
}
