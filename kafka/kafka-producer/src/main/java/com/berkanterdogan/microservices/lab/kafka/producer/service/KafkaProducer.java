package com.berkanterdogan.microservices.lab.kafka.producer.service;

import com.berkanterdogan.microservices.lab.kafka.model.base.BaseKafkaMessageModelDto;

import java.io.Serializable;

public interface KafkaProducer<K extends Serializable, V extends BaseKafkaMessageModelDto> {
    void send(String topicName, K key, V message);
}
