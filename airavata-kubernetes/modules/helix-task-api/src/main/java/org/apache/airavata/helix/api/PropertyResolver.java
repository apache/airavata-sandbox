package org.apache.airavata.helix.api;

import java.io.*;
import java.util.Properties;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
public class PropertyResolver {

    private Properties properties = new Properties();

    public void loadFromFile(File propertyFile) throws IOException {
        properties = new Properties();
        properties.load(new FileInputStream(propertyFile));
    }

    public void loadInputStream(InputStream inputStream) throws IOException {
        properties = new Properties();
        properties.load(inputStream);
    }

    public String get(String key) {
        if (properties.containsKey(key)) {
            if (System.getenv(key.replace(".", "_")) != null) {
                return System.getenv(key.replace(".", "_"));
            } else {
                return properties.getProperty(key);
            }
        } else {
            return null;
        }
    }

}
