#!/usr/bin/env bash
kafka_dir="/Users/zhouguangping/Downloads/kafka_2.13-2.7.1"
cd $kafka_dir

bin/zookeeper-server-start.sh -daemon config/zookeeper.properties
sleep 20
bin/kafka-server-start.sh -daemon config/server0.properties
bin/kafka-server-start.sh -daemon config/server1.properties
bin/kafka-server-start.sh -daemon config/server2.properties