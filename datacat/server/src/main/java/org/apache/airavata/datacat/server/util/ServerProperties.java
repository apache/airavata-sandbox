package org.apache.airavata.datacat.server.util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;

public class ServerProperties {
    public static final String SERVER_PROPERTY_FILE = "../conf/server.properties";
    public static final String DEFAULT_SERVER_PROPERTY_FILE = "conf/server.properties";

    private static ServerProperties instance;

    private final Logger logger = LogManager.getLogger(ServerProperties.class);

    private java.util.Properties properties = null;

    private ServerProperties() {
        try {
            InputStream fileInput;
            if (new File(SERVER_PROPERTY_FILE).exists()) {
                fileInput = new FileInputStream(SERVER_PROPERTY_FILE);
                logger.info("Using configured server property (server.properties) file");
            } else {
                logger.info("Using default server property (server.properties) file");
                fileInput = ClassLoader.getSystemResource(DEFAULT_SERVER_PROPERTY_FILE).openStream();
            }
            java.util.Properties properties = new java.util.Properties();
            properties.load(fileInput);
            fileInput.close();
            this.properties = properties;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ServerProperties getInstance() {
        if (instance == null) {
            instance = new ServerProperties();
        }
        return instance;
    }

    public String getProperty(String key, String defaultVal) {
        String val = this.properties.getProperty(key);

        if (val.isEmpty() || val == "") {
            return defaultVal;
        } else {
            return val;
        }
    }
}
