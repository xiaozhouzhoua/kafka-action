package com.kafka.interceptors;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ProducerInterceptorPrefix implements ProducerInterceptor<String, String> {
    private AtomicInteger sendSuccess = new AtomicInteger();
    private AtomicInteger sendFailure = new AtomicInteger();

    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
        String modifiedValue = "prefix1-" + record.value();
        return new ProducerRecord<>(record.topic(), record.partition(), record.timestamp(),
                record.key(), modifiedValue, record.headers());
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        if (exception == null) {
            sendSuccess.getAndIncrement();
        } else {
            sendFailure.getAndIncrement();
        }
    }

    @Override
    public void close() {
        double successRatio = (double)sendSuccess.get() / (sendFailure.get() + sendSuccess.get());
        System.out.println("[INFO] 发送成功率=" + String.format("%f", successRatio * 100) + "%");
    }

    @Override
    public void configure(Map<String, ?> configs) {
    }
}
