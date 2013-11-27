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
 *     Jirka Daněk <dnk@mail.muni.cz>
 *     Biserka Cvetkovska
 *     Ivana Kostadinovska
 */

package fri.cwb.ThermodynamicSimulationEngine;
import fri.cbw.GenericTool.AbstractEngineTool;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 */
@ServiceProvider(service = AbstractEngineTool.class)
public class ThermodynamicSimulationEngine extends AbstractEngineTool{

    @Override
    public void calculate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public String getName() {
        return "Thermodynamic Simulation Engine";
    }

    @Override
    public String getAuthor() {
        return "";
    }

    @Override
    public Class getTopComponentClass() {
        throw new UnsupportedOperationException("Not supported yet.");
    }  
    
}
