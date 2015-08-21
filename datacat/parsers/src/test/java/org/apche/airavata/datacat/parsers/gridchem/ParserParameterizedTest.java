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

import junit.framework.Assert;
import org.apache.airavata.datacat.parsers.gridchem.B3PW91.B3PW91Parser;
import org.apache.airavata.datacat.parsers.gridchem.GridChemQueueParser;
import org.apache.airavata.datacat.parsers.gridchem.cbsQ.CbsQParser;
import org.apache.airavata.datacat.parsers.gridchem.finalcoord.FinalCoordParser;
import org.apache.airavata.datacat.parsers.gridchem.g1.G1Parser;
import org.apache.airavata.datacat.parsers.gridchem.gaussian.GaussianParser;
import org.apache.airavata.datacat.parsers.gridchem.gfinalcoord.GFinalCoordParser;
import org.apache.airavata.datacat.parsers.gridchem.gmcscfa.GmcscfaParser;
import org.apache.airavata.datacat.parsers.gridchem.gnumatom.GNumAtomParser;
import org.apache.airavata.datacat.parsers.gridchem.gopt.GoptParser;
import org.apache.airavata.datacat.parsers.gridchem.gscfa.GscfaParser;
import org.apache.airavata.datacat.parsers.gridchem.gvb.GVBParser;
import org.apache.airavata.datacat.parsers.gridchem.input.InputParser;
import org.apache.airavata.datacat.parsers.gridchem.method.MethodParser;
import org.apache.airavata.datacat.parsers.gridchem.mfinalcoord.MFinalCoordParser;
import org.apache.airavata.datacat.parsers.gridchem.mopta.MOptaParser;
import org.apache.airavata.datacat.parsers.gridchem.mp2to5a.MP2to5aParser;
import org.apache.airavata.datacat.parsers.gridchem.mp2to5b.MP2to5bParser;
import org.apache.airavata.datacat.parsers.gridchem.mp2to5c.MP2to5cParser;
import org.apache.airavata.datacat.parsers.gridchem.mp2to5d.MP2to5dParser;
import org.apache.airavata.datacat.parsers.gridchem.scfa.SCFaParser;
import org.apache.airavata.datacat.parsers.gridchem.scfb.SCFbParser;
import org.apache.airavata.datacat.parsers.gridchem.util.Constants;
import org.apache.airavata.datacat.parsers.gridchem.util.GridChemProperties;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

@RunWith(Parameterized.class)
public class ParserParameterizedTest {
    private Logger log = Logger.getLogger(ParserParameterizedTest.class);

    private String filename;
    private boolean expected;
    private GridChemQueueParser parser;
    private String OUTPUT_FILE_PATH;


    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"2H2OOHNCmin.com.out", true},
                {"2H2OOHNCtsb.com.out", true},
                {"2H2OOHNCtsc.com.out", true},
                {"2H2OOHNCtsd.com.out", true},
                {"2H2OOHNCtse.com.out", true},
                {"2H2OOHNCtsf.com.out", true},
                {"A1MBBTSNTd.com.out",true},
                {"A1MBBTSNTe.com.out",true},
                {"AcArgTS1.com.out",true},
                {"AcArgTS1a.com.out",true},
                {"AcArgTS1mi.com.out",true},
                {"AcArgTS2.com.out",true},
                {"AcArTS1mia.com.out",true},
                {"acetbvTS1.com.out",true},
                {"acetbvTS1a.com.out",true},
                {"acet_bvTSa.com.out",true},
                {"acetbvTSHM.com.out",true},
                {"acetbvTSHM1.com.out",true},
                {"acetHGS.com.out",true},
                {"acetHTS1.com.out",true},
                {"acetHTS1IR.com.out",true},
                {"acetHTS1IRR.com.out",true},
                {"acetHTS2.com.out",true},
                {"acettetTS.com.out",true},
                {"acettetTSa.com.out",true},
                {"acettetTSM.com.out",true},
                {"acettetTSM1.com.out",true},
                {"acetTS1Ha.com.out",true},
                {"acetTSaIRCF.com.out",true},
                {"AchextetB.com.out",true},
                {"AcOHOHTS.com.out",true},
                {"AcOHOHTS1.com.out",true},
                {"AcPhCHtetB.com.out",true},
                {"ActethexA.com.out",true},
                {"ActetPhA.com.out",true},
                {"BalvTSIRCF.com.out",true},
                {"BalvTSIRCR.com.out",true},
                {"CH3oohGS.com.out",true},
                {"CH3oohtet.com.out",true},
                {"CH3oohTS1.com.out",true},
                {"CH3oohTS1a.com.out",true},
                {"CH4_311.com.out",true},
                {"cycAGS1.com.out",true},
                {"cycATS1.com.out",true},
                {"cycAGS1.com.out",true},
                {"cycBTS1.com.out",true},
                {"FL_acOHts4.com.out",true},
                {"FLarcyctsF.com.out",true},
                {"FLox31_311.com.out",true},
                {"H2O_OHNaCl.com.out",true},
                {"H2OOHNaClts.com.out",true},
                {"H2OOHNCltsa.com.out",true},
                {"H2OOHNCltsb.com.out",true},
                {"H2OOHNCltsc.com.out",true},
                {"H2O_OHrad.com.out",true},
                {"H2O_OHradN.com.out",true},
                {"H2OOHradNts.com.out",true},
                {"H2O_OHradts.com.out",true},
                {"H3O_ONaCl.com.out",true},
                {"IQOOGSMeOH.com.out",true},
                {"IQOOGSMeOHa.com.out",true},
                {"IQOOGSMeOHb.com.out",true},
                {"IQOOH.com.out",true},
                {"IQOOHBcIRCF.com.out",true},
                {"IQOOHBcIRCR.com.out",true},
                {"IQOOHBGIRCF.com.out",true},
                {"IQOOHBGIRCR.com.out",true},
                {"IQOOHimts.com.out",true},
                {"IQOOHimtsa.com.out",true},
                {"IQOOHimtsb.com.out",true},
                {"IQOOHIRCF.com.out",true},
                {"IQOOHIRCF6.com.out",true},
                {"IQOOHIRCFa.com.out",true},
                {"IQOOHIRCFre.com.out",true},
                {"IQOOHIRCR.com.out",true},
                {"IQOOHIRCR6.com.out",true},
                {"IQOOHNHCOOH.com.out",true},
                {"IQOOHNHIRCF.com.out",true},
                {"IQOOHNHIRCR.com.out",true},
                {"IQOOHNmin.com.out",true},
                {"IQOOHNmin90.com.out",true},
                {"IQOOHNminUF.com.out",true},
                {"IQOOHNts.com.out",true}
        });
    }

    public ParserParameterizedTest(String filename, boolean expected) {
        OUTPUT_FILE_PATH = GridChemProperties.getInstance().getProperty(Constants.SAMPLE_OUTPUT, "");
        this.filename = OUTPUT_FILE_PATH +"/"+ filename+"/"+filename;
        this.expected = expected;


    }

    @Test
    public void testGOPTParser() {
        try {
            parser = new GoptParser(new FileReader(this.filename));
            HashMap<String, String> parsedData = parser.getParsedData();
            Assert.assertEquals(expected, parsedData != null);

           log.info("Parse successful in GoptParser"+filename);

        } catch (FileNotFoundException e) {
          //  log.error("FileNotFound in ..." + e.getMessage());
        } catch (Exception e) {
          //  log.error("Error occurred in parsing " + filename + " : " + e.getMessage());
        }
    }
    @Test
    public void testB3PW91() {
        try {
            parser = new B3PW91Parser(new FileReader(this.filename));
            HashMap<String, String> parsedData = parser.getParsedData();
            Assert.assertEquals(expected, parsedData != null);
            log.info("Parse successful in B3PW91"+filename);
        } catch (FileNotFoundException e) {
          //  log.error("FileNotFound in ..." + e.getMessage());
        } catch (Exception e) {
          //  log.error("Error occurred in parsing " + filename + " : " + e.getMessage());
        }
    }
    @Test
    public void testCbsqParser() {
        try {
            parser = new CbsQParser(new FileReader(this.filename));
            HashMap<String, String> parsedData = parser.getParsedData();
            Assert.assertEquals(expected, parsedData != null);
            log.info("Parse successful in Cbsq "+filename);
        } catch (FileNotFoundException e) {
           // log.error("FileNotFound in ..." + e.getMessage());
        } catch (Exception e) {
           // log.error("Error occurred in parsing " + filename + " : " + e.getMessage());
        }
    }

    @Test
    public void testFinalCoordParser() {
        try {
            parser = new FinalCoordParser(new FileReader(this.filename));
            HashMap<String, String> parsedData = parser.getParsedData();
            Assert.assertEquals(expected, parsedData != null);
            log.info("Parse successful in FinalCoord " + filename);
        } catch (FileNotFoundException e) {
            // log.error("FileNotFound in ..." + e.getMessage());
        } catch (Exception e) {
            // log.error("Error occurred in parsing " + filename + " : " + e.getMessage());
        }
    }

    @Test
    public void testG1Parser() {
        try {
            parser = new G1Parser(new FileReader(this.filename));
            HashMap<String, String> parsedData = parser.getParsedData();
            Assert.assertEquals(expected, parsedData != null);
            log.info("Parse successful in G1 " + filename);
        } catch (FileNotFoundException e) {
            // log.error("FileNotFound in ..." + e.getMessage());
        } catch (Exception e) {
            // log.error("Error occurred in parsing " + filename + " : " + e.getMessage());
        }
    }

    @Test
    public void testGaussianParser() {
        try {
            parser = new GaussianParser(new FileReader(this.filename));
            HashMap<String, String> parsedData = parser.getParsedData();
            Assert.assertEquals(expected, parsedData != null);
            log.info("Parse successful in Gaussian " + filename);
        } catch (FileNotFoundException e) {
            // log.error("FileNotFound in ..." + e.getMessage());
        } catch (Exception e) {
            // log.error("Error occurred in parsing " + filename + " : " + e.getMessage());
        }
    }

    @Test
    public void testGFinalCoord() {
        try {
            parser = new GFinalCoordParser(new FileReader(this.filename));
            HashMap<String, String> parsedData = parser.getParsedData();
            Assert.assertEquals(expected, parsedData != null);
            log.info("Parse successful in GfinalCoord Parser " + filename);
        } catch (FileNotFoundException e) {
            // log.error("FileNotFound in ..." + e.getMessage());
        } catch (Exception e) {
            // log.error("Error occurred in parsing " + filename + " : " + e.getMessage());
        }
    }

    @Test
    public void testGmcsfaParser() {
        try {
            parser = new GmcscfaParser(new FileReader(this.filename));
            HashMap<String, String> parsedData = parser.getParsedData();
            Assert.assertEquals(expected, parsedData != null);
            log.info("Parse successful in Gmcsfa Parser " + filename);
        } catch (FileNotFoundException e) {
            // log.error("FileNotFound in ..." + e.getMessage());
        } catch (Exception e) {
            // log.error("Error occurred in parsing " + filename + " : " + e.getMessage());
        }
    }

    @Test
    public void testGvbParser() {
        try {
            parser = new GVBParser(new FileReader(this.filename));
            HashMap<String, String> parsedData = parser.getParsedData();
            Assert.assertEquals(expected, parsedData != null);
            log.info("Parse successful in Gvb Parser " + filename);
        } catch (FileNotFoundException e) {
            // log.error("FileNotFound in ..." + e.getMessage());
        } catch (Exception e) {
            // log.error("Error occurred in parsing " + filename + " : " + e.getMessage());
        }
    }

    @Test
    public void testGscfaParser() {
        try {
            parser = new GscfaParser(new FileReader(this.filename));
            HashMap<String, String> parsedData = parser.getParsedData();
            Assert.assertEquals(expected, parsedData != null);
            log.info("Parse successful in Gscfa Parser " + filename);
        } catch (FileNotFoundException e) {
            // log.error("FileNotFound in ..." + e.getMessage());
        } catch (Exception e) {
            // log.error("Error occurred in parsing " + filename + " : " + e.getMessage());
        }
    }

    @Test
    public void testGnumatomParser() {
        try {
            parser = new GNumAtomParser(new FileReader(this.filename));
            HashMap<String, String> parsedData = parser.getParsedData();
            Assert.assertEquals(expected, parsedData != null);
            log.info("Parse successful in GnumAtom Parser " + filename);
        } catch (FileNotFoundException e) {
            // log.error("FileNotFound in ..." + e.getMessage());
        } catch (Exception e) {
            // log.error("Error occurred in parsing " + filename + " : " + e.getMessage());
        }
    }

    @Test
    public void testInputParser() {
        try {
            parser = new InputParser(new FileReader(this.filename));
            HashMap<String, String> parsedData = parser.getParsedData();
            Assert.assertEquals(expected, parsedData != null);
            log.info("Parse successful in Input Parser " + filename);
        } catch (FileNotFoundException e) {
            // log.error("FileNotFound in ..." + e.getMessage());
        } catch (Exception e) {
            // log.error("Error occurred in parsing " + filename + " : " + e.getMessage());
        }
    }

    @Test
    public void testMethodParser() {
        try {
            parser = new MethodParser(new FileReader(this.filename));
            HashMap<String, String> parsedData = parser.getParsedData();
            Assert.assertEquals(expected, parsedData != null);
            log.info("Parse successful in MethodParser " + filename);
        } catch (FileNotFoundException e) {
            // log.error("FileNotFound in ..." + e.getMessage());
        } catch (Exception e) {
            // log.error("Error occurred in parsing " + filename + " : " + e.getMessage());
        }
    }

    @Test
    public void testMFinalCoordParser() {
        try {
            parser = new MFinalCoordParser(new FileReader(this.filename));
            HashMap<String, String> parsedData = parser.getParsedData();
            Assert.assertEquals(expected, parsedData != null);
            log.info("Parse successful in MFinalCoord Parser " + filename);
        } catch (FileNotFoundException e) {
            // log.error("FileNotFound in ..." + e.getMessage());
        } catch (Exception e) {
            // log.error("Error occurred in parsing " + filename + " : " + e.getMessage());
        }
    }

    @Test
    public void TestMoptaParser() {
        try {
            parser = new MOptaParser(new FileReader(this.filename));
            HashMap<String, String> parsedData = parser.getParsedData();
            Assert.assertEquals(expected, parsedData != null);
            log.info("Parse successful in Mopta Parser " + filename);
        } catch (FileNotFoundException e) {
            log.error("FileNotFound in ..." + e.getMessage());
        } catch (Exception e) {
            log.error("Error occurred in parsing " + filename + " : " + e.getMessage());
        }
    }

    @Test
    public void TestMp2to5aParser() {
        try {
            parser = new MP2to5aParser(new FileReader(this.filename));
            HashMap<String, String> parsedData = parser.getParsedData();
            Assert.assertEquals(expected, parsedData != null);
            log.info("Parse successful in Mp2to5a Parser " + filename);
        } catch (FileNotFoundException e) {
            log.error("FileNotFound in ..." + e.getMessage());
        } catch (Exception e) {
            log.error("Error occurred in parsing " + filename + " : " + e.getMessage());
        }
    }


    @Test
    public void TestMp2to5bParser() {
        try {
            parser = new MP2to5bParser(new FileReader(this.filename));
            HashMap<String, String> parsedData = parser.getParsedData();
            Assert.assertEquals(expected, parsedData != null);
            log.info("Parse successful in Mp2to5b Parser " + filename);
        } catch (FileNotFoundException e) {
            log.error("FileNotFound in ..." + e.getMessage());
        } catch (Exception e) {
            log.error("Error occurred in parsing " + filename + " : " + e.getMessage());
        }
    }

    @Test
    public void TestMp2to5cParser() {
        try {
            parser = new MP2to5cParser(new FileReader(this.filename));
            HashMap<String, String> parsedData = parser.getParsedData();
            Assert.assertEquals(expected, parsedData != null);
            log.info("Parse successful in Mp2to5c Parser " + filename);
        } catch (FileNotFoundException e) {
            log.error("FileNotFound in ..." + e.getMessage());
        } catch (Exception e) {
            log.error("Error occurred in parsing " + filename + " : " + e.getMessage());
        }
    }

    @Test
    public void TestMp2to5dParser() {
        try {
            parser = new MP2to5dParser(new FileReader(this.filename));
            HashMap<String, String> parsedData = parser.getParsedData();
            Assert.assertEquals(expected, parsedData != null);
            log.info("Parse successful in Mp2to5d Parser " + filename);
        } catch (FileNotFoundException e) {
            log.error("FileNotFound in ..." + e.getMessage());
        } catch (Exception e) {
            log.error("Error occurred in parsing " + filename + " : " + e.getMessage());
        }
    }

    @Test
    public void TestScfaParser() {
        try {
            parser = new SCFaParser(new FileReader(this.filename));
            HashMap<String, String> parsedData = parser.getParsedData();
            Assert.assertEquals(expected, parsedData != null);
            log.info("Parse successful in Scfa Parser " + filename);
        } catch (FileNotFoundException e) {
            log.error("FileNotFound in ..." + e.getMessage());
        } catch (Exception e) {
            log.error("Error occurred in parsing " + filename + " : " + e.getMessage());
        }
    }

    @Test
    public void TestScfbParser() {
        try {
            parser = new SCFbParser(new FileReader(this.filename));
            HashMap<String, String> parsedData = parser.getParsedData();
            Assert.assertEquals(expected, parsedData != null);
            log.info("Parse successful in Scfb Parser " + filename);
        } catch (FileNotFoundException e) {
            log.error("FileNotFound in ..." + e.getMessage());
        } catch (Exception e) {
            log.error("Error occurred in parsing " + filename + " : " + e.getMessage());
        }
    }
}
