package org.apache.airavata.agents.core;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
public class StatusPublisher {
    private String brokerUrl;
    private String topicName;

    private Producer<String, String> eventProducer;

    public StatusPublisher(String brokerUrl, String topicName) {
        this.brokerUrl = brokerUrl;
        this.topicName = topicName;
        this.initializeKafkaEventProducer();
    }

    public void publishStatus(long callbackWorkflowId, AsyncCommandStatus status, String message) {
        this.eventProducer.send(new ProducerRecord<String, String>(
                this.topicName, String.join(",", callbackWorkflowId + "", status.name(), message)));
    }

    public void initializeKafkaEventProducer() {
        Properties props = new Properties();

        props.put("bootstrap.servers", this.brokerUrl);

        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");

        eventProducer = new KafkaProducer<String, String>(props);
    }
}
