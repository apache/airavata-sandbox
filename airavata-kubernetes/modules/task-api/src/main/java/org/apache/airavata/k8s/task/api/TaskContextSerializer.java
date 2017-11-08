package org.apache.airavata.k8s.task.api;

import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
public class TaskContextSerializer implements Serializer<TaskContext> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String topic, TaskContext data) {
        return new byte[0];
    }

    @Override
    public void close() {

    }
}
