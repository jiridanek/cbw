/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.MyTestEnginTool1;

import fri.cbw.CBWutil.InboundConnectionException;
import fri.cbw.GenericTool.AbstractEngineTool;
import fri.cbw.GenericTool.AbstractModelTool;
import fri.cbw.GenericTool.AbstractSpecies;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import org.netbeans.api.visual.graph.GraphScene;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Sašo
 */

@ServiceProvider(service = AbstractEngineTool.class)
public class MyTestEnginTool1 extends AbstractEngineTool{
    
    @Override
    public void calculate() {
        calculateData();
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
    
    
    private void calculateData() {
        try {        
            AbstractModelTool mt = (AbstractModelTool)getFirstInboundModul();
            LinkedHashMap<AbstractSpecies, Double> species = mt.getSpecies();
            
            NumberAxis xAxis = new NumberAxis();
            xAxis.setLabel("Čas");
            NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel("Količina");
            
            XYChart<Number,Number> lineChart = new LineChart<Number,Number>(xAxis,yAxis);
            lineChart.setTitle("Testni podatki MyTestEnginTool");
            
            int cas = 20;
            
            for(AbstractSpecies as : species.keySet()){
                XYChart.Series series = new XYChart.Series();
                series.setName(as.getName());
                
                for(int i = 0; i <= cas; i++){    
                    series.getData().add(new XYChart.Data(i, Math.random()*100));
                }
                lineChart.getData().add(series);
            }
            
            setLineChartData(lineChart);
            
        } catch (Exception ex) {
            Logger.getLogger(MyTestEnginTool1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
