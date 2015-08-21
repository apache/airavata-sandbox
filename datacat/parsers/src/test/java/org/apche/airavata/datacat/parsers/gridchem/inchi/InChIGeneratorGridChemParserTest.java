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
package org.apche.airavata.datacat.parsers.gridchem.inchi;

import junit.framework.Assert;
import org.apache.airavata.datacat.parsers.gridchem.inchi.InChI;
import org.apache.airavata.datacat.parsers.gridchem.inchi.InChIGenerator;
import org.apache.airavata.datacat.parsers.gridchem.util.Constants;
import org.apache.airavata.datacat.parsers.gridchem.util.GridChemProperties;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apche.airavata.datacat.parsers.gridchem.AbstractGridChemParserTest;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class InChIGeneratorGridChemParserTest extends AbstractGridChemParserTest {

    private Logger logger = Logger.getLogger(InChIGeneratorGridChemParserTest.class);

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testGetInChI() throws Exception {
        try {
            String uniqueID = UUID.randomUUID().toString();
            File source = new File(TEST_OUTPUT);

            File dest = new File(GridChemProperties.getInstance().getProperty(
                    Constants.TMP_DIR,"") + "/" + uniqueID + ".out");
            FileUtils.copyFile(source, dest);

            InChIGenerator inChIGenerator = new InChIGenerator(uniqueID);
            InChI inChI = inChIGenerator.getInChI();

            logger.info("TEST MESSAGE");
            logger.info(inChI.getInchi() + " inchi key: " + inChI.getInchiKey());
            Assert.assertNotNull(inChI.getInchi());
            Assert.assertNotNull(inChI.getInchiKey());
        } catch (IOException ignore) {
            ignore.printStackTrace();
        }
    }
}