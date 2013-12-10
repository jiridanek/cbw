/*
 * (C) Copyright 2013 Computational Biology Workspace (http://lrss.fri.uni-lj.si/bio/cbw.html)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Contributors:
 *     David Petrunov
 *     Aleksander Bešir
 *     Jirka Daněk <dnk@mail.muni.cz>
 *     Biserka Cvetkovska
 *     Ivana Kostadinovska
 */

package fri.cbw.ThermodynamicSimulationEngine;
import fri.cbw.CBWutil.InboundConnectionException;
import fri.cbw.GenericTool.AbstractEngineTool;
import fri.cbw.GenericTool.AbstractGenericTool;
import fri.cbw.GenericTool.AbstractModelTool;
import fri.cbw.GenericTool.ToolTopComponent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import org.openide.util.lookup.ServiceProvider;
import fri.cbw.ThermodynamicModelTool.ThermodynamicModelTool;
import fri.cbw.ThermodynamicModelTool.ThermodynamicModelToolTopComponent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

/**
 *
 */
@ServiceProvider(service = AbstractEngineTool.class)
public class ThermodynamicSimulationEngine extends AbstractEngineTool{

    @Override
    public void calculate() {
        try {
        AbstractGenericTool prev;
        ThermodynamicModelTool tmt;
        ThermodynamicModelToolTopComponent tmttc;
        try {
            prev = this.getFirstInboundModul();
            ToolTopComponent tc = prev.getTc();
            if (tc instanceof ThermodynamicModelToolTopComponent) {
                tmttc = (ThermodynamicModelToolTopComponent) tc;
            } else {
                // FIXME: this happens if the ThermodynamicModelToolTopComponent window is not currently open
                DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message("Previous module to ThermodynamicSimulationEngine must be a ThermodynamicModelTool"));
                return;
            }
        }catch(InboundConnectionException e){
               Logger.getLogger(ThermodynamicSimulationEngine.class.getName()).log(Level.SEVERE, null, e);
                DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message("ThermodynamicSimulationEngine must have a ThermodynamicModelTool as predecessor"));
                return;
            }

        int effectiveRNAPMoleculNumber = tmttc.getEffectiveRNAPMoleculNumber();
        double differenceRNAPEnergy = tmttc.getDifferenceRNAPEnergy();
        int numberOfNSBindSites = tmttc.getNumberOfNSBindSites();

        Vector<Vector> regulatorsDefinitions = tmttc.getRegulatorsDefinitions();
        Vector<Vector> bindingSitesData = tmttc.getBindingSitesData();
        Vector<Vector> bindingSitesInteractionNumber = tmttc.getBindingSitesInteractionNumber();

        TranscriptionUnit unit = new TranscriptionUnit(effectiveRNAPMoleculNumber, (float) differenceRNAPEnergy, numberOfNSBindSites);
        for (Vector r : regulatorsDefinitions) {
            String name = (String) r.elementAt(0);
            String type = (String) r.elementAt(1); //"Activator"/"Represor"
            float rp_energy = Float.parseFloat((String) r.elementAt(2));
            int number = Integer.parseInt((String) r.elementAt(3), 10);

            unit.addRegulator(name, rp_energy);
            unit.setRegMolNumber(name, number);
        }
        for (Vector s : bindingSitesData) {
            int id = Integer.parseInt((String) s.elementAt(0), 10);
            String reg = (String) s.elementAt(1);
            float dEnergy = Float.parseFloat((String) s.elementAt(2)); // FIXME: index?
            unit.addBindingSite(id, reg, dEnergy);
        }
        for (Vector i : bindingSitesInteractionNumber) {
            int bs1 = Integer.parseInt((String) i.elementAt(0), 10);
            int bs2 = Integer.parseInt((String) i.elementAt(1), 10);
            SortedSet set = new TreeSet<Integer>(Arrays.asList(bs1, bs2));
            String type = (String) i.elementAt(2);
            if (type.equalsIgnoreCase("Overlap")) {
                unit.addForbiddenEvent(set);
                continue;
            }
            float g_energy = Float.parseFloat((String) i.elementAt(3));
            float gp_energy = Float.parseFloat((String) i.elementAt(4));
            unit.addInteractGroup(set, g_energy, gp_energy);
        }

        DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message(
                unit.bindingProb()
        ));
        System.out.println(unit.bindingProb());
        //throw new UnsupportedOperationException("Not supported yet.");
        } catch(Throwable e) {
            Writer result = new StringWriter();
            PrintWriter printWriter = new PrintWriter(result);
            e.printStackTrace(printWriter);
            DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message(
                result.toString()
            ));
            e.printStackTrace(System.out);
        }
    }
    
    @Override
    public String getName() {
        return "Thermodynamic Simulation Engine";
    }

    @Override
    public String getAuthor() {
        return "PETRUNOV, BEŠIR";
    }

    @Override
    public Class<Object> getTopComponentClass() {
        calculate();
        throw new UnsupportedOperationException("Not supported yet.");
    }  
    
}
