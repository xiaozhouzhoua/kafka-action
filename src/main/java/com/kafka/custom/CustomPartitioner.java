package com.kafka.custom;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.InvalidRecordException;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.utils.Utils;

import java.util.List;
import java.util.Map;

/**
 * 自定义分区器
 */
public class CustomPartitioner implements Partitioner {
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        List<PartitionInfo> partitionInfos = cluster.partitionsForTopic(topic);
        int partitionSize = partitionInfos.size();
        //如果key为空或者不是字符串，抛异常
        if (null == keyBytes || !(key instanceof String)) {
            throw new InvalidRecordException("Kafka message must have key");
        }

        //如果分区数是1的话，那只能返回第一个分区了
        if (partitionSize == 1) {
            return 0;
        }

        //如果key名称为banana，选择最后一个分区
        if (key.equals("banana")) {
            return partitionSize - 1;
        }
        //使用Kafka的工具类根据大小和分区数取模计算出分区数
        System.out.println(Utils.murmur2(keyBytes));
        return Math.abs(Utils.murmur2(keyBytes)) % (partitionSize - 1);
    }

    @Override
    public void close() {
    }

    @Override
    public void configure(Map<String, ?> map) {
    }
}
