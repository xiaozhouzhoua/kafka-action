#!/usr/bin/env bash
kafka_dir="/Users/zhouguangping/Downloads/kafka_2.13-2.7.1"
cd $kafka_dir

bin/kafka-server-stop.sh
bin/zookeeper-server-stop.sh