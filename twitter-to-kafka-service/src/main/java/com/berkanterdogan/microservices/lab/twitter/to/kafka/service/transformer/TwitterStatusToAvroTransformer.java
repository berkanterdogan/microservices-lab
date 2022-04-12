package com.berkanterdogan.microservices.lab.twitter.to.kafka.service.transformer;

import com.berkanterdogan.microservices.lab.kafka.model.avro.TwitterAvroModel;
import org.springframework.stereotype.Component;
import twitter4j.Status;

@Component
public class TwitterStatusToAvroTransformer implements AvroTransformer<TwitterAvroModel, Status>{

    @Override
    public TwitterAvroModel transformToAvroModel(Status objectToBeTransformed) {
        return TwitterAvroModel.newBuilder()
                .setId(objectToBeTransformed.getId())
                .setUserId(objectToBeTransformed.getUser().getId())
                .setText(objectToBeTransformed.getText())
                .setCreatedAt(objectToBeTransformed.getCreatedAt().getTime())
                .build();
    }
}
