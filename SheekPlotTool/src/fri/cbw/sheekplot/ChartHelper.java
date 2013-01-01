/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.sheekplot;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;

/**
 *
 * @author miha
 */
public class ChartHelper {
    public static ArrayList<LineChart.Series<Double,Double>> getSeriesFromResults(String[] names, double[] t, double[][] results){

        ArrayList<LineChart.Series<Double,Double>> lineSeries = new ArrayList<LineChart.Series<Double,Double>>(names.length);
        
        for (int i = 0; i < names.length; i++) {
            ArrayList<Data<Double,Double>> seriesArray = new ArrayList<Data<Double,Double>>(results.length);
            for (int j = 0; j < results.length; j++) {
                seriesArray.add(new Data<Double, Double>(t[j],results[j][i]));
            }
            lineSeries.add(new LineChart.Series<Double,Double>(names[i], FXCollections.observableArrayList(seriesArray)));
        }
        
        return lineSeries;
        
    }
    
    public static LineChart createLineChart(String xAxisName, String yAxisName, ArrayList<LineChart.Series<Double,Double>> lineChartData){
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel(xAxisName);
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(yAxisName);
        
        LineChart lineChart = new LineChart(xAxis, yAxis);
        lineChart.setCreateSymbols(false);
        
        lineChart.getData().addAll(lineChartData);
        
        return lineChart;
    }
    
    public static double[][] summarizeData(double[][][] data, String sumFunction, int skipStart){
        
        double[][] retVal=new double[data[skipStart].length][data.length-skipStart];
        
        for (int i = skipStart; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                double summedValue="MIN".equals(sumFunction)?Double.MAX_VALUE:0;
                for (int k = 0; k < data[i][j].length; k++) {
                    double value=data[i][j][k];
                    if("SUM".equals(sumFunction)||"AVG".equals(sumFunction)){
                        summedValue+=Math.abs(value);
                    }
                    else if("MIN".equals(sumFunction)){
                        if(Math.abs(value)<summedValue) {
                            summedValue=Math.abs(value);
                        }
                    }
                    else if("MAX".equals(sumFunction)){
                        if(Math.abs(value)>summedValue) {
                            summedValue=Math.abs(value);
                        }
                    }
                }
                if("AVG".equals(sumFunction)){
                    summedValue/=data[i][j].length;
                }
                retVal[j][i-skipStart]=summedValue;
            }
        }
        return retVal;
    }
}
