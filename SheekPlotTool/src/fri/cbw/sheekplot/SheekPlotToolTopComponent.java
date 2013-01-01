/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.sheekplot;

import fri.cbw.GenericTool.AbstractGenericTool;
import fri.cbw.GenericTool.ToolTopComponent;
import fri.cbw.ToolGraph.ToolWrapper;
import java.awt.BorderLayout;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import org.netbeans.api.settings.ConvertAsProperties;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

/**
 * Top component which displays something.
 */
@TopComponent.Description(
    preferredID = "SheekPlotToolTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_NEVER)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "fri.cbw.sheekplot.SheekPlotToolTopComponent")
@Messages({
    "CTL_SheekPlotToolAction=SheekPlotTool",
    "CTL_SheekPlotToolTopComponent=SheekPlotTool Window",
    "HINT_SheekPlotToolTopComponent=This is a SheekPlotTool window"
})
public final class SheekPlotToolTopComponent extends ToolTopComponent {
    
    
    
    public SheekPlotToolTopComponent(GraphScene scene, IconNodeWidget toolNode) {
        super(scene, toolNode);
        initComponents();
        setName(Bundle.CTL_SheekPlotToolTopComponent());
        setToolTipText(Bundle.HINT_SheekPlotToolTopComponent());
        
        Platform.setImplicitExit(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        cbSummarize = new javax.swing.JCheckBox();
        comboSumFunction = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        spinnerSkipCharts = new javax.swing.JSpinner();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        tabPane = new javax.swing.JTabbedPane();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 2));

        org.openide.awt.Mnemonics.setLocalizedText(cbSummarize, org.openide.util.NbBundle.getMessage(SheekPlotToolTopComponent.class, "SheekPlotToolTopComponent.cbSummarize.text")); // NOI18N
        cbSummarize.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cbSummarize.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel1.add(cbSummarize);

        comboSumFunction.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SUM", "MIN", "MAX", "AVG" }));
        jPanel1.add(comboSumFunction);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(SheekPlotToolTopComponent.class, "SheekPlotToolTopComponent.jLabel1.text")); // NOI18N
        jPanel1.add(jLabel1);

        spinnerSkipCharts.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(0), null, Integer.valueOf(1)));
        jPanel1.add(spinnerSkipCharts);

        org.openide.awt.Mnemonics.setLocalizedText(jButton1, org.openide.util.NbBundle.getMessage(SheekPlotToolTopComponent.class, "SheekPlotToolTopComponent.jButton1.text")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);

        org.openide.awt.Mnemonics.setLocalizedText(jButton2, org.openide.util.NbBundle.getMessage(SheekPlotToolTopComponent.class, "SheekPlotToolTopComponent.jButton2.text")); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2);

        add(jPanel1, java.awt.BorderLayout.PAGE_END);
        add(tabPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
        ToolWrapper tw=(ToolWrapper) this.getToolWrapper();
        
        ToolWrapper twPrev=tw.getPrevNode(this.getScene());
        
        AbstractGenericTool abTool=twPrev.getNodeGenericTool();
        
        if(!(abTool instanceof SheekPlotEngine)) {
            DialogDisplayer.getDefault().notify(
                new NotifyDescriptor.Message("Engine must be Sheek to display Sheek plots (interface SheekPlotEngine)."));
            return;
        }
        
        SheekPlotEngine engine=(SheekPlotEngine)abTool;
        
        if(!engine.getResultsAvailable()){
            DialogDisplayer.getDefault().notify(
                new NotifyDescriptor.Message("Engine results are not available."));
            return;
        }
        
        String[] chartTitles=engine.getChartTitles();
        String[][] seriesNames=engine.getResultNames();
        double[] tSamples=engine.gettSamples();
        String[] xAxisNames=engine.getxAxisNames();
        String[] yAxisNames=engine.getyAxisNames();
        
        double[][][] results=engine.getResults();
        
        boolean summarizeCharts=cbSummarize.isSelected();
        String sumFunction=(String)comboSumFunction.getSelectedItem();
        int skipChartsForSum=(Integer)spinnerSkipCharts.getValue();
        
        tabPane.removeAll();
        final int chartCount;
        int regularChartCount;
        
        if(!summarizeCharts){
            chartCount = chartTitles.length;
            regularChartCount=chartCount;
        }
        else{
            chartCount=skipChartsForSum+1;
            regularChartCount=skipChartsForSum;
        }
        
        
        final JFXPanel[] chartPanels=new JFXPanel[chartCount];
        final LineChart[] lineCharts=new LineChart[chartCount];
        for (int i = 0; i < regularChartCount; i++) {
            chartPanels[i]=new JFXPanel();
            tabPane.addTab(chartTitles[i], chartPanels[i]);

            ArrayList<LineChart.Series<Double, Double>> series = ChartHelper.getSeriesFromResults(seriesNames[i], tSamples, results[i]);
            lineCharts[i] = ChartHelper.createLineChart(xAxisNames[i], yAxisNames[i], series);
            lineCharts[i].setTitle(chartTitles[i]);
            
        }
        
        if(summarizeCharts){
            double[][] summarizedData=ChartHelper.summarizeData(results, sumFunction, skipChartsForSum);
            chartPanels[chartCount-1]=new JFXPanel();
            tabPane.addTab("Summarized chart", chartPanels[chartCount-1]);
            String[] sumSeriesNames=new String[chartTitles.length-skipChartsForSum];
            System.arraycopy(chartTitles, skipChartsForSum, sumSeriesNames, 0, sumSeriesNames.length);
            ArrayList<LineChart.Series<Double, Double>> series = ChartHelper.getSeriesFromResults(sumSeriesNames, tSamples, summarizedData);
            lineCharts[chartCount-1] = ChartHelper.createLineChart(xAxisNames[skipChartsForSum], yAxisNames[skipChartsForSum], series);
            lineCharts[chartCount-1].setTitle("Summarized chart");
        }
        
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < chartCount; i++) {
                    chartPanels[i].setScene(new Scene(lineCharts[i]));
                }
            }
        });
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                tabPane.removeAll();
            }
        });
    }//GEN-LAST:event_jButton2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox cbSummarize;
    private javax.swing.JComboBox comboSumFunction;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSpinner spinnerSkipCharts;
    private javax.swing.JTabbedPane tabPane;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }
}
