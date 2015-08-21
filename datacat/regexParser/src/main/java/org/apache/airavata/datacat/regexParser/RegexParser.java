package org.apache.airavata.datacat.regexParser;

import org.apache.airavata.datacat.models.OutputMetadataDTO;
import org.apache.airavata.datacat.models.OutputMonitorMessage;
import org.apache.airavata.datacat.parsers.DefaultParser;
import org.apache.airavata.datacat.parsers.IParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexParser extends DefaultParser implements IParser {
    private static Logger log = LoggerFactory.getLogger(RegexParser.class);
    private Map<String, String> regexMap;

    /**
     * parses a file using the provided regular expression
     * @param file
     * @param regex
     * @return (String) extracted value
     * @throws IOException
     */
    public String parseRegex(String file, String regex) throws IOException {
        String fileContent = readFile(file, null);
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(fileContent);

        String result = "";
        while (m.find() && m.find()) {

            //return one value
            if (m.groupCount() == 1)
                result = m.group(1);

            //allowing it to return a string of values
            else if (m.groupCount() > 1)
                for (int i = 0; i < m.groupCount(); i++)
                    result = m.group(i) + "\t";
            else
                result = null;
        }
        return result;
    }

    /**
     * Reads a file
     * @param path
     * @param encoding
     * @return the file converted to a String
     * @throws IOException
     */
    private String readFile(String path, Charset encoding)
            throws IOException {
        if (encoding == null) {
            encoding = Charset.defaultCharset();
        }
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    /**
     * Parses the file
     * @param outputMonitorMessage
     * @return OutputMetadataDTO
     */
    @Override
    public OutputMetadataDTO parse(OutputMonitorMessage outputMonitorMessage) {
        OutputMetadataDTO outputMetadataDTO = super.parse(outputMonitorMessage);
        HashMap<String, String> customMetadata = new HashMap<>();

        //looking for the gaussian output files
        String[] outPutFiles = (new File(outputMonitorMessage.getOutputPath())).list(new FilenameFilter() {
            public boolean accept(File dir, String filename) {
                return filename.endsWith(".out");
            }
        });
        if (outPutFiles != null && outPutFiles.length > 0) {
            String outFile = outputMonitorMessage.getOutputPath() + File.separator + outPutFiles[0];
            String uniqueID = UUID.randomUUID().toString();

            //parsing the file using regex
            String result = "";

            //reading the regexes provided in the properties file
            regexMap = RegexParserProperties.getInstance().getRegexMap();
            for (Map.Entry<String, String> entry : regexMap.entrySet()) {
                try {
                    result = parseRegex(outFile, entry.getValue());
                    if (result == null || result.length() < 1)
                        continue;
                } catch (IOException e) {
                    log.error("Error occured while parsing the regex " + e.toString());
                }
                customMetadata.put(entry.getKey(), result);
            }
        }
        outputMetadataDTO.setCustomMetaData(customMetadata);

        return outputMetadataDTO;
    }


}
