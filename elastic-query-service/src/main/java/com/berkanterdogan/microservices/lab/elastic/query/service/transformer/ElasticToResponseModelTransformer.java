package com.berkanterdogan.microservices.lab.elastic.query.service.transformer;

import com.berkanterdogan.microservices.lab.elastic.model.index.impl.TwitterIndexModel;
import com.berkanterdogan.microservices.lab.elastic.query.service.model.ElasticQueryServiceResponseModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ElasticToResponseModelTransformer {

    public ElasticQueryServiceResponseModel transformResponseModel(TwitterIndexModel twitterIndexModel) {
        return ElasticQueryServiceResponseModel.builder()
                .id(twitterIndexModel.getId())
                .userId(twitterIndexModel.getUserId())
                .text(twitterIndexModel.getText())
                .createdAt(twitterIndexModel.getCreatedAt())
                .build();
    }

    public List<ElasticQueryServiceResponseModel> transformResponseModels(List<TwitterIndexModel> twitterIndexModels) {
        return twitterIndexModels.stream().map(this::transformResponseModel)
                .collect(Collectors.toList());
    }
}
