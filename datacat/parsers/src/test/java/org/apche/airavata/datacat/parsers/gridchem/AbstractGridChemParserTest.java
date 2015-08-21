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
package org.apche.airavata.datacat.parsers.gridchem;

import org.apache.airavata.datacat.parsers.gridchem.util.Constants;
import org.apache.airavata.datacat.parsers.gridchem.util.GridChemProperties;
import org.apache.log4j.Logger;
import org.junit.Before;

import java.util.Properties;

public abstract class AbstractGridChemParserTest {
    protected Properties testProperties;
    protected String TEST_OUTPUT;
    private Logger logger = Logger.getLogger(AbstractGridChemParserTest.class);

    @Before
    public void setUp() {
        TEST_OUTPUT = GridChemProperties.getInstance().getProperty(Constants.SAMPLE_OUTPUT,"");
    }
}