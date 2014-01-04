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
 */
package fri.cbw.ThermodynamicSimulationEngine;

import fri.cbw.CBWutil.InboundConnectionException;
import fri.cbw.GenericTool.AbstractEngineTool;
import fri.cbw.GenericTool.AbstractGenericTool;
import fri.cbw.GenericTool.ToolTopComponent;
import fri.cbw.ThermodynamicModelTool.ThermodynamicModelTool;
import fri.cbw.ThermodynamicModelTool.ThermodynamicModelToolTopComponent;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

/**
 * Numerical simulation of an experiment done by Knorre in 1968. The model is
 * described in article Influence of Catabolite Repression and Inducer Exclusion
 * on the Bistable Behavior of the lac Operon.
 */
public class Knorre1968 {

}
