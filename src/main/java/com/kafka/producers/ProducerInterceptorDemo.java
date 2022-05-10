package com.kafka.producers;

import com.kafka.entity.Customer;
import com.kafka.interceptors.ProducerInterceptorPrefix;
import com.kafka.serializer.CustomerSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ProducerInterceptorDemo {
    public static final String brokerList = "localhost:9092,localhost:9093,localhost:9094";
    public static final String topic = "kinaction_helloworld";

    public static Properties initConfig(){
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, ProducerInterceptorPrefix.class.getName());
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "producer.client.id.demo");
        return props;
    }

    public static void main(String[] args) {
        Properties props = initConfig();
        try (KafkaProducer<String, String> producer = new KafkaProducer<>(props)) {
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, "拦截器测试");
            Future<RecordMetadata> future = producer.send(record);
            RecordMetadata metadata = future.get();
            System.out.println(metadata.topic() + "-" +
                    metadata.partition() + ":" + metadata.offset());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
