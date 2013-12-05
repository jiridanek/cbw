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
 */

package fri.cbw.ThermodynamicModelTool;
import fri.cbw.GenericTool.AbstractModelTool;
// import fri.cbw.GenericTool.AbstractThermodynamicModelTool;
import org.openide.util.lookup.ServiceProvider;


/**
 *
 * @author Petrunov, Bešir
 */
@ServiceProvider(service=AbstractModelTool.class)
public class ThermodynamicModelTool extends AbstractModelTool { // used to be AbstractThermodynamicModelTool

    @Override
    public String getName() {
        return "Thermodynamic Model Tool";
    }

    @Override
    public String getAuthor() {
        return "PETRUNOV, BEŠIR";
    }

    @Override
    public Class getTopComponentClass() {
        return ThermodynamicModelToolTopComponent.class;
    }

}
