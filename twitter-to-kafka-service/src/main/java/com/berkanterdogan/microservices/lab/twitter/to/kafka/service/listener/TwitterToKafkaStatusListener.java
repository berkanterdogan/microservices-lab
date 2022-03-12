package com.berkanterdogan.microservices.lab.twitter.to.kafka.service.listener;

import com.berkanterdogan.microservices.lab.app.config.data.kafka.KafkaConfigData;
import com.berkanterdogan.microservices.lab.kafka.model.TwitterKafkaMessageModelDto;
import com.berkanterdogan.microservices.lab.kafka.producer.service.KafkaProducer;
import com.berkanterdogan.microservices.lab.twitter.to.kafka.service.transformer.KafkaNessageModelDtoTransformer;
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
    private final KafkaProducer<Long, TwitterKafkaMessageModelDto> kafkaProducer;
    private final KafkaNessageModelDtoTransformer<TwitterKafkaMessageModelDto, Status> modelDtoTransformer;

    @Override
    public void onStatus(Status status) {
        String topicName = this.kafkaConfigData.getTwitterTopicName();
        log.info("Received status text {} sending to kafka topic {}", status.getText(), topicName);
        TwitterKafkaMessageModelDto twitterKafkaMessageModelDto = modelDtoTransformer.transformToModelDto(status);
        long messageKey = twitterKafkaMessageModelDto.getUserId();
        this.kafkaProducer.send(topicName, messageKey, twitterKafkaMessageModelDto);
    }
}
