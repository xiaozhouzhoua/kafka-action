package com.kafka.consumers;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 分区数需要事先知晓，可以将 consumerThreadNum 设置成不大于分区数的值，
 * 如果不知道主题的分区数，那么也可以通过 KafkaConsumer 类的
 * partitionsFor() 方法来间接获取，进而再设置合理的 consumerThreadNum 值。
 * 此方式的优点是每个线程可以按顺序消费各个分区中的消息。
 * 缺点也很明显，每个消费线程都要维护一个独立的TCP连接，
 * 如果分区数和 consumerThreadNum 的值都很大，那么会造成不小的系统开销。
 */
public class FirstMultiConsumerThreadDemo {
    public static final String brokerList = "localhost:9092,localhost:9093,localhost:9094";
    public static final String topic = "kinaction_helloworld";
    public static final String groupId = "group.demo";
    public static final AtomicBoolean isRunning = new AtomicBoolean(true);

    public static Properties initConfig(){
        Properties props = new Properties();
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, "consumer.client.id.demo");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        return props;
    }

    public static void main(String[] args) {
        Properties props = initConfig();
        int consumerThreadNum = 4;
        for(int i = 0; i < consumerThreadNum; i++) {
            new KafkaConsumerThread(props, topic).start();
        }
    }

    public static class KafkaConsumerThread extends Thread {
        private KafkaConsumer<String, String> kafkaConsumer;

        public KafkaConsumerThread(Properties props, String topic) {
            this.kafkaConsumer = new KafkaConsumer<>(props);
            this.kafkaConsumer.subscribe(Arrays.asList(topic));
        }

        @Override
        public void run() {
            try {
                while(isRunning.get()) {
                    ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));
                    records.forEach(record -> {
                        System.out.println("topic = " + record.topic()
                                + ", partition = "+ record.partition()
                                + ", offset = " + record.offset());
                        System.out.println("key = " + record.key()
                                + ", value = " + record.value());
                        // 处理逻辑
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                kafkaConsumer.close();
            }
        }
    }
}
