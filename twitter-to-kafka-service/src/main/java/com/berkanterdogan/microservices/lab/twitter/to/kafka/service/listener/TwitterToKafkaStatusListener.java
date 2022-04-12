package com.berkanterdogan.microservices.lab.twitter.to.kafka.service.listener;

import com.berkanterdogan.microservices.lab.app.config.data.kafka.KafkaConfigData;
import com.berkanterdogan.microservices.lab.kafka.model.avro.TwitterAvroModel;
import com.berkanterdogan.microservices.lab.kafka.producer.service.KafkaProducer;
import com.berkanterdogan.microservices.lab.twitter.to.kafka.service.transformer.AvroTransformer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import twitter4j.Status;
import twitter4j.StatusAdapter;

@Slf4j
@RequiredArgsConstructor
@Component
public class TwitterToKafkaStatusListener extends StatusAdapter {

    private final KafkaConfigData kafkaConfigData;
    private final KafkaProducer<Long, TwitterAvroModel> kafkaProducer;
    private final AvroTransformer<TwitterAvroModel, Status> avroTransformer;

    @Override
    public void onStatus(Status status) {
        String topicName = this.kafkaConfigData.getTopicName();
        log.info("Received status text {} sending to kafka topic {}", status.getText(), topicName);
        TwitterAvroModel twitterAvroModel = avroTransformer.transformToAvroModel(status);
        long messageKey = twitterAvroModel.getUserId();
        this.kafkaProducer.send(topicName, messageKey, twitterAvroModel);
    }
}
