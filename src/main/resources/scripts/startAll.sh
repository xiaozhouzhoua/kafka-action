#!/usr/bin/env bash
kafka_dir="/Users/zhouguangping/Downloads/kafka_2.13-2.7.1"
cd $kafka_dir

lsof -t -i tcp:2181 | xargs kill -9
lsof -t -i tcp:9092 | xargs kill -9
lsof -t -i tcp:9093 | xargs kill -9
lsof -t -i tcp:9094 | xargs kill -9

rm -f ./kafka-logs/kafka-logs-0/meta.properties
rm -f ./kafka-logs/kafka-logs-1/meta.properties
rm -f ./kafka-logs/kafka-logs-2/meta.properties

bin/zookeeper-server-start.sh -daemon config/zookeeper.properties
sleep 20
bin/kafka-server-start.sh -daemon config/server0.properties
bin/kafka-server-start.sh -daemon config/server1.properties
bin/kafka-server-start.sh -daemon config/server2.properties