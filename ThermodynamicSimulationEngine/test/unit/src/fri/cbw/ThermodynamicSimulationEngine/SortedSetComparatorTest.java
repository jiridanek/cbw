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
import java.util.TreeSet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * 
 */
public class SortedSetComparatorTest {
    @Test
    public void testEqual() {
        TranscriptionUnit.SortedSetComparator<Integer> cmp = new TranscriptionUnit.SortedSetComparator<Integer>();
        // equal
        assertEquals(0, cmp.compare(new TreeSet<Integer>(
                Arrays.asList(1,2,3)
        ), new TreeSet<Integer>(
                Arrays.asList(3,2,1)
        )));
    }
    @Test
    public void testNoElements() {
        TranscriptionUnit.SortedSetComparator<Integer> cmp = new TranscriptionUnit.SortedSetComparator<Integer>();

        // first smaller
        assertTrue(cmp.compare(new TreeSet<Integer>(
                Arrays.asList(1,2,3)
        ), new TreeSet<Integer>(
                Arrays.asList(1,2,3,4)
        )) < 0);

        // first larger
        assertTrue(cmp.compare(new TreeSet<Integer>(
                Arrays.asList(1,2,3,4)
        ), new TreeSet<Integer>(
                Arrays.asList(1,2,3)
        )) > 0);
    }
    @Test
    public void testValues() {
        TranscriptionUnit.SortedSetComparator<Integer> cmp = new TranscriptionUnit.SortedSetComparator<Integer>();

        // first smaller
        assertTrue(cmp.compare(new TreeSet<Integer>(
                Arrays.asList(1,2,3)
        ), new TreeSet<Integer>(
                Arrays.asList(1,2,4)
        )) < 0);

        // first larger
        assertTrue(cmp.compare(new TreeSet<Integer>(
                Arrays.asList(1,2,4)
        ), new TreeSet<Integer>(
                Arrays.asList(1,2,3)
        )) > 0);
    }
}
