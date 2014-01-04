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
 *     Biserka Cvetkovska
 *     Jirka Daněk <dnk@mail.muni.cz>
 */

package fri.cbw.thermodynamicparamevaltool;

import fri.cbw.GenericTool.AbstractParamEvalTool;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = AbstractParamEvalTool.class)
public class ThermodynamicParamEvalTool extends AbstractParamEvalTool {

    @Override
    public String getName() {
        return "Thermodynamic Param Eval Tool";
    }

    @Override
    public String getAuthor() {
        return "Biserka Cvetkovska, Ivana Kostadinovska, and Jirka Daněk";
    }

    @Override
    public Class getTopComponentClass() {
        return ThermodynamicParamEvalToolTopComponent.class;
    }

}
