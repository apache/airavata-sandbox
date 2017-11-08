package org.apache.airavata.k8s.task.api;

import org.apache.kafka.common.serialization.Deserializer;

import java.io.*;
import java.util.Map;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
public class TaskContextDeserializer implements Deserializer<TaskContext> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public TaskContext deserialize(String topic, byte[] data) {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            return(TaskContext)in.readObject();
        } catch (IOException e) {
            // ignore exception
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return null;
    }

    @Override
    public void close() {

    }
}
