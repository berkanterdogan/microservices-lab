package com.berkanterdogan.microservices.lab.elastic.query.service.business.impl;

import com.berkanterdogan.microservices.lab.elastic.model.index.impl.TwitterIndexModel;
import com.berkanterdogan.microservices.lab.elastic.query.client.service.ElasticQueryClient;
import com.berkanterdogan.microservices.lab.elastic.query.service.business.ElasticQueryService;
import com.berkanterdogan.microservices.lab.elastic.query.service.model.ElasticQueryServiceResponseModel;
import com.berkanterdogan.microservices.lab.elastic.query.service.model.assembler.ElasticQueryServiceResponseModelAssembler;
import com.berkanterdogan.microservices.lab.elastic.query.service.transformer.ElasticToResponseModelTransformer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class TwitterElasticQueryService implements ElasticQueryService {

    private final ElasticQueryServiceResponseModelAssembler elasticQueryServiceResponseModelAssembler;
    private final ElasticQueryClient<TwitterIndexModel> elasticQueryClient;

    @Override
    public List<ElasticQueryServiceResponseModel> getAllDocuments() {
        log.info("Querying all documents in elasticsearch");
        List<TwitterIndexModel> result = elasticQueryClient.getAllIndexModels();
        return elasticQueryServiceResponseModelAssembler.toModels(result);
    }

    @Override
    public ElasticQueryServiceResponseModel getDocumentById(String id) {
        log.info("Querying elasticsearch by id {}", id);
        TwitterIndexModel result = elasticQueryClient.getIndexModelById(id);
        return elasticQueryServiceResponseModelAssembler.toModel(result);
    }

    @Override
    public List<ElasticQueryServiceResponseModel> getDocumentsByText(String text) {
        log.info("Querying elasticsearch by text {}", text);
        List<TwitterIndexModel> result = elasticQueryClient.getIndexModelByText(text);
        return elasticQueryServiceResponseModelAssembler.toModels(result);
    }
}
