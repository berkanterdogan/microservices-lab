package com.berkanterdogan.microservices.lab.twitter.to.kafka.service.transformer;

import com.berkanterdogan.microservices.lab.kafka.model.TwitterKafkaMessageModelDto;
import org.springframework.stereotype.Component;
import twitter4j.Status;

@Component
public class TwitterStatusToKafkaMessageModelDtoTransformer implements KafkaNessageModelDtoTransformer<TwitterKafkaMessageModelDto, Status> {

    @Override
    public TwitterKafkaMessageModelDto transformToModelDto(Status objectToBeTransformed) {
        TwitterKafkaMessageModelDto twitterKafkaMessageModelDto = new TwitterKafkaMessageModelDto();
        twitterKafkaMessageModelDto.setId(objectToBeTransformed.getId());
        twitterKafkaMessageModelDto.setUserId(objectToBeTransformed.getUser().getId());
        twitterKafkaMessageModelDto.setText(objectToBeTransformed.getText());
        twitterKafkaMessageModelDto.setCreatedAt(objectToBeTransformed.getCreatedAt().getTime());
        return twitterKafkaMessageModelDto;
    }
}
