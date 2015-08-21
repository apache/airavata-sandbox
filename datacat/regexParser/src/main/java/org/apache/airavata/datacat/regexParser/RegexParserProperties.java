package org.apache.airavata.datacat.regexParser;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class RegexParserProperties {

    //location of the properties file
    public static final String REGEX_PARSER_PROPERTIES = "../conf/regexParser.properties";
    public static final String DEFAULT_REGEX_PARSER_PROPERTIES = "regexParser.properties";

    private static RegexParserProperties instance;

    private final Logger logger = LogManager.getLogger(RegexParserProperties.class);
    private Map<String, String> properties;
    private Map<String, String> regexMap;

    // the singleton method for the RegexParserConstructor
    public static RegexParserProperties getInstance() {
        if (instance == null) {
            instance = new RegexParserProperties();
        }
        return instance;
    }

    private RegexParserProperties() {
        try {
            //resolute the correct property file
            InputStream fileInput;
            if (new File(REGEX_PARSER_PROPERTIES).exists()) {
                fileInput = new FileInputStream(REGEX_PARSER_PROPERTIES);
            } else {
                fileInput = ClassLoader.getSystemResource(DEFAULT_REGEX_PARSER_PROPERTIES).openStream();
            }
            //load the properties file and extract the regular expressions as a Hashmap
            this.properties = getProperties(fileInput);
            fileInput.close();

            //initialize the map of regular expressions
            this.regexMap = new HashMap<String, String>();
            try {
                for (Map.Entry<String, String> entry : this.properties.entrySet()) {
                    this.regexMap.put(entry.getKey(), entry.getValue());
                }
            } catch (Exception e) {
                logger.error("Error occured while reading regexes from file " + e.getMessage());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method reads an input stream, and returns the regular expressions as a Map
     * @param fileInputStream
     * @return Map of regular expressions with the provided tokens
     * @throws IOException
     */
    private Map<String, String> getProperties(InputStream fileInputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
        String line;
        Map<String, String> result = new HashMap<String, String>();
        while ((line = reader.readLine()) != null) {
            if (line.trim().length() == 0 || line.startsWith("#"))
                continue;

            //extract the key value pairs of the property file
            String key = line.replaceFirst("([^=]+)=(.*)", "$1");
            String val = line.replaceFirst("([^=]+)=(.*)", "$2");
            result.put(key, val);

        }
        reader.close();
        return result;
    }

    public Map<String, String> getRegexMap() {
        return regexMap;
    }
}
