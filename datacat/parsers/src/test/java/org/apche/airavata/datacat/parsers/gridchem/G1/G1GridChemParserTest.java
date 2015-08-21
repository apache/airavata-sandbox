/*
*
* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements.  See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership.  The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License.  You may obtain a copy of the License at
*
*   http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*
*/
package org.apche.airavata.datacat.parsers.gridchem.G1;

import junit.framework.Assert;
import org.apche.airavata.datacat.parsers.gridchem.AbstractGridChemParserTest;
import org.apache.airavata.datacat.parsers.gridchem.g1.G1Parser;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.Map;

public class G1GridChemParserTest extends AbstractGridChemParserTest {
    private Logger logger = Logger.getLogger(G1GridChemParserTest.class);

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testG1Parser() {
        boolean exception = false;
        try {
            G1Parser p = new G1Parser(new java.io.FileReader(TEST_OUTPUT));
            Map<String, String> results = p.getParsedData();
            logger.info("======= Printing the results ==========");
            logger.info(results.toString());
        } catch (FileNotFoundException e) {
            logger.error("File Not Found Exception ..."+e.getMessage());
            exception = true;
        } catch (Exception e) {
            logger.error("Parse Exception ..." + e.getMessage());
            exception = true;
        }
        Assert.assertFalse(exception);
    }
}
