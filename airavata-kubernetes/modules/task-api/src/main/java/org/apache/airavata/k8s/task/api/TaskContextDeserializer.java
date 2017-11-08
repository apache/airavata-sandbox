package org.apache.airavata.k8s.task.api;

import org.apache.kafka.common.serialization.Deserializer;

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
        return null;
    }

    @Override
    public void close() {

    }
}
