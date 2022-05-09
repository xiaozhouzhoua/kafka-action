## Creating our Topic

```shell script
export TOPIC=kinaction_helloworld
```

```shell script
bin/kafka-topics.sh --create --bootstrap-server localhost:9092,localhost:9093,localhost:9094 \
  --topic $TOPIC --partitions 3 --replication-factor 3
```

## Describe the Topic

```shell script
bin/kafka-topics.sh --bootstrap-server localhost:9092,localhost:9093,localhost:9094 --describe --topic $TOPIC
```

## Kafka Producer Console Command

```shell script
bin/kafka-console-producer.sh --bootstrap-server localhost:9092,localhost:9093,localhost:9094 --topic $TOPIC
```

## Kafka Consumer Console Command

```shell script
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092,localhost:9093,localhost:9094 \
--topic $TOPIC --from-beginning
```