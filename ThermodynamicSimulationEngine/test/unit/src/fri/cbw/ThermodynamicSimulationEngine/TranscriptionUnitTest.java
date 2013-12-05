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

import fri.cbw.ThermodynamicSimulationEngine.TranscriptionUnit;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

public class TranscriptionUnitTest {

    // TODO: make few tests for this, at least test
        // every branch in special cases
        // some borderline values: number = 0, number = N; number != 2 ;-)
        //  N = 0; N != 2
    // possibly copy from Python testsuite ;-)
    @Test
    @Ignore
    public void testCombine() {
        System.out.println("combine");
        int number = 0;
        TranscriptionUnit instance = null;
        Set<Set<Integer>> expResult = null;
        //<Set<Integer>> result = instance.combine(number);
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
    private final double delta = 1e-6;

    @Test
    public void tCalExampleBasal() {
        TranscriptionUnit unit = new TranscriptionUnit(1000, -5.0f, 5e6f);
        assertEquals(0.028826971440739344, unit.baseBindingProb(), delta);
    }

    @Test
    public void tCalExampleNoBinding() {
        TranscriptionUnit unit = new TranscriptionUnit(1000, -5.0f, 5e6f);
        assertEquals(unit.baseBindingProb(), unit.bindingProb(), delta);
    }
    
    @Test
    public void tCalExampleNormal() {
        TranscriptionUnit unit = new TranscriptionUnit(1000, -5.0f, 5e6f);
        // ADD CHEMICALS
            unit.addRegulator("A", -4);
            unit.addRegulator("B", -3.5f);
            unit.setRegMolNumber("A", 100);
            unit.setRegMolNumber("B", 200);
            unit.addBindingSite(1, "A", -7);
            unit.addBindingSite(2, "B", -7);
        assertEquals(0.13040871277175775, unit.bindingProb(), delta);
    }
    
    @Test
    public void tCalExampleInteractGroup() {
        TranscriptionUnit unit = new TranscriptionUnit(1000, -5.0f, 5e6f);
        // ADD CHEMICALS
            unit.addRegulator("A", -4);
            unit.addRegulator("B", -3.5f);
            unit.setRegMolNumber("A", 100);
            unit.setRegMolNumber("B", 200);
            unit.addBindingSite(1, "A", -7);
            unit.addBindingSite(2, "B", -7);
        unit.addInteractGroup(new TreeSet<Integer>(Arrays.asList(1,2)),10,10);
        assertEquals(0.092275549028488557, unit.bindingProb(), delta);
    }
    @Test
    public void tCalExampleForbiddenEvent() {
        TranscriptionUnit unit = new TranscriptionUnit(1000, -5.0f, 5e6f);
        // ADD CHEMICALS
            unit.addRegulator("A", -4);
            unit.addRegulator("B", -3.5f);
            unit.setRegMolNumber("A", 100);
            unit.setRegMolNumber("B", 200);
            unit.addBindingSite(1, "A", -7);
            unit.addBindingSite(2, "B", -7);
        unit.addForbiddenEvent(new TreeSet<Integer>(Arrays.asList(1,2)));
        assertEquals(0.092275552378896578, unit.bindingProb(), delta);
    }
    
    @Test
    public void tCalExampleNoB() {
        TranscriptionUnit unit = new TranscriptionUnit(1000, -5.0f, 5000000f);
        // ADD CHEMICALS
            unit.addRegulator("A", -.5f);
            unit.addRegulator("B", -.5f);
            unit.addBindingSite(1, "A", -7);
            unit.addBindingSite(2, "B", -7);
        unit.addInteractGroup(new TreeSet<Integer>(Arrays.asList(1,2)),-5,-5);
        unit.setRegMolNumber("A", 300);
        unit.setRegMolNumber("B", 0);
        assertEquals(0.029946900183255565, unit.bindingProb(), delta);
    }
    
    @Test
    public void tCalExampleHaveBoth() {
        TranscriptionUnit unit = new TranscriptionUnit(1000, -5.0f, 5000000f);
        // ADD CHEMICALS
            unit.addRegulator("A", -.5f);
            unit.addRegulator("B", -.5f);
            unit.addBindingSite(1, "A", -7);
            unit.addBindingSite(2, "B", -7);
        unit.addInteractGroup(new TreeSet<Integer>(Arrays.asList(1,2)),-5,-5);
        unit.setRegMolNumber("A", 300);
        unit.setRegMolNumber("B", 300);
        assertEquals(0.81333798227294996, unit.bindingProb(), delta);
    }
    
    @Test
    public void tCalExampleFormula() {
        TranscriptionUnit unit = new TranscriptionUnit(1000, -5.0f, 5000000f);
        // ADD CHEMICALS
            unit.addRegulator("A", -.5f);
            unit.addRegulator("B", -.5f);
            unit.addBindingSite(1, "A", -7);
            unit.addBindingSite(2, "B", -7);
        unit.addInteractGroup(new TreeSet<Integer>(Arrays.asList(1,2)),-5,-5);
        unit.setRegMolNumber("A", 300);
        unit.setRegMolNumber("B", 300);
        assertEquals("1/(1+33689.734995/(1000*((1+A*0.000362+B*0.000362+1*" +
                "((A/5000000)^1)*((B/5000000)^1)*72004899337.385880)/(1+A*" +
                "0.000219+B*0.000219+1*((A/5000000)^1)*((B/5000000)^1)*" +
                "178482300.963187))))", unit.bindingProbFormula());
    }  
}
