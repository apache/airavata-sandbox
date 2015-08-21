package org.apache.airavata.datacat.regexParser;

import junit.framework.Assert;
import org.apache.airavata.datacat.models.OutputMetadataDTO;
import org.apache.airavata.datacat.models.OutputMonitorMessage;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class RegexParserTest {
    private static Logger log = LoggerFactory.getLogger(RegexParserTest.class);

    // Configure the output files here
    private final static String FILEPATH = "/data-root/scnakandala/Gaussian/Regex_test_6IDontNeedaNode_1db684c9-8641-472a-8e85-0aaebc37d6ff/";
    private final static String FILE = "/data-root/scnakandala/Gaussian/Regex_test_6IDontNeedaNode_1db684c9-8641-472a-8e85-0aaebc37d6ff/gaussian1.out";

    private RegexParser regexParser;

    @Before
    public void setup() {
        regexParser = new RegexParser();
    }

    /*
     * This method tests the complete functionality of the regexParser
     *
     */
    @Test
    public void testParser() {
        //create a dummy output monitor message
        OutputMonitorMessage message = new OutputMonitorMessage();
        message.setApplicationName("Gaussian");
        message.setOutputPath(FILEPATH);
        message.setExperimentID("Sachith");

        //parse it to the parser
        OutputMetadataDTO outputMetadataDTO = regexParser.parse(message);

        System.out.println(String.valueOf(outputMetadataDTO.getCustomMetaData().entrySet()));
        Assert.assertNotNull(outputMetadataDTO.getCustomMetaData().entrySet());

    }

    /*
    * This method tests if the parseRegex Method works properly
     */
    @Test
    public void testParseRegex() throws IOException {
        String s = regexParser.parseRegex(FILE, "RMS\\s+Force\\s+(\\d*\\.?\\d*)");
        log.debug(s);
        Assert.assertNotNull(s);
    }

}
