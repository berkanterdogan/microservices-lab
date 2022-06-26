package com.berkanterdogan.microservices.lab.kafka.to.elastic.service.consumer;

import org.apache.avro.specific.SpecificRecordBase;

import java.util.List;

public interface KafkaConsumer<V extends SpecificRecordBase> {
    void receive(List<V> messages, List<Integer> keys, List<Integer> partitions, List<Long> offsets);
}
