/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.TauLeapingEngine;

import fri.cbw.GenericTool.AbstractEngineTool;
import fri.cbw.GenericTool.AbstractModelTool;
import fri.cbw.GenericTool.AbstractReactionType;
import fri.cbw.ToolGraph.ToolWrapper;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import org.netbeans.api.visual.graph.GraphScene;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Boštjan Cigan
 */

@ServiceProvider(service = AbstractEngineTool.class)
public class TauLeapingEngine extends AbstractEngineTool{
    
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
        return "Tau Leaping Engine";
    }

    @Override
    public String getAuthor() {
        return "Boštjan Cigan, Jernej Hartman, Domen Mladovan";
    }

    @Override
    public Class getTopComponentClass() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    

    private void calculateData(Object tool, GraphScene scene) {
        ToolWrapper prev = ((ToolWrapper)tool).getPrevNode(scene);
        AbstractModelTool mt = (AbstractModelTool) prev.getNodeGenericTool();
        
        List<String> species = mt.getSpecies();
        List<AbstractReactionType> lala = mt.getReactions();
        
        for(int i=0; i<lala.size(); i++) {
            TauReaction tr = (TauReaction) lala.get(i);
            System.out.println("Prsu sem notr");
            System.out.println(tr.getM());
        }
        
        LineChart.Series<Double,Double> [] es = new LineChart.Series[species.size()];
        
        for (int i = 0; i < species.size(); i++) {
            es[i] = new LineChart.Series<Double,Double>(species.get(i), FXCollections.observableArrayList(
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
