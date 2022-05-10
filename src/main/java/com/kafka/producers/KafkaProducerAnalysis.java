package com.kafka.producers;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

public class KafkaProducerAnalysis {
    public static final String brokerList = "localhost:9092,localhost:9093,localhost:9094";
    public static final String topic = "kinaction_helloworld";

    public static Properties initConfig(){
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "producer.client.id.demo");
        return props;
    }

    public static void main(String[] args) {
        Properties props = initConfig();
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        IntStream.rangeClosed(0, 100).forEach(i -> {
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, "msg" + i);
            try {
                Future<RecordMetadata> future = producer.send(record);
                RecordMetadata metadata = future.get();
                System.out.println(metadata.topic() + "-" +
                        metadata.partition() + ":" + metadata.offset());
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        producer.close();
    }
}
