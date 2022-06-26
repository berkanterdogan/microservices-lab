package com.berkanterdogan.microservices.lab.kafka.to.elastic.service.transformer;

import com.berkanterdogan.microservices.lab.elastic.model.index.impl.TwitterIndexModel;
import com.berkanterdogan.microservices.lab.kafka.model.avro.TwitterAvroModel;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AvroToElasticModelTransformer implements ElasticModelTransformer<TwitterIndexModel, TwitterAvroModel>{

    @Override
    public List<TwitterIndexModel> transformToElasticModel(List<TwitterAvroModel> avroModels) {
        return avroModels.stream()
                .map(avroModel -> TwitterIndexModel.builder()
                        .userId(avroModel.getUserId())
                        .id(String.valueOf(avroModel.getId()))
                        .text(avroModel.getText())
                        .createdAt(
                                LocalDateTime.ofInstant(
                                        Instant.ofEpochMilli(avroModel.getCreatedAt()), ZoneId.systemDefault()
                                )
                        )
                        .build()
                ).collect(Collectors.toList());
    }
}
