package com.berkanterdogan.microservices.lab.kafka.producer.service.impl;

import com.berkanterdogan.microservices.lab.kafka.model.TwitterKafkaMessageModelDto;
import com.berkanterdogan.microservices.lab.kafka.producer.callback.KafkaListenableFutureCallback;
import com.berkanterdogan.microservices.lab.kafka.producer.service.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Slf4j
@RequiredArgsConstructor
@Service
public class TwitterKafkaProducer implements KafkaProducer<Long, TwitterKafkaMessageModelDto> {

    private final KafkaTemplate<Long, TwitterKafkaMessageModelDto> kafkaTemplate;

    @Override
    public void send(String topicName, Long key, TwitterKafkaMessageModelDto message) {
        log.info("Sending message='{}' to topic='{}'", message, topicName);
        ListenableFuture<SendResult<Long, TwitterKafkaMessageModelDto>> kafkaResultFeature = kafkaTemplate.send(topicName, key, message);
        kafkaResultFeature.addCallback(new KafkaListenableFutureCallback<>(topicName, key, message));
    }
}
