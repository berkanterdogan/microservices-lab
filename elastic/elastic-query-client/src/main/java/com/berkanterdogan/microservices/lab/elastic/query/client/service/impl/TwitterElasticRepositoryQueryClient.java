package com.berkanterdogan.microservices.lab.elastic.query.client.service.impl;

import com.berkanterdogan.microservices.lab.common.util.CollectionsUtil;
import com.berkanterdogan.microservices.lab.elastic.query.client.exception.ElasticQueryClientException;
import com.berkanterdogan.microservices.lab.elastic.query.client.repository.TwitterElasticSearchQueryRepository;
import com.berkanterdogan.microservices.lab.elastic.query.client.service.ElasticQueryClient;
import com.berkanterdogan.microservices.lab.elastic.model.index.impl.TwitterIndexModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Primary
@Service
public class TwitterElasticRepositoryQueryClient implements ElasticQueryClient<TwitterIndexModel> {

    private final TwitterElasticSearchQueryRepository twitterElasticSearchQueryRepository;

    @Override
    public TwitterIndexModel getIndexModelById(String id) {

        Optional<TwitterIndexModel> searchResult = twitterElasticSearchQueryRepository.findById(id);
        TwitterIndexModel twitterIndexModel = searchResult.orElseThrow(() -> new ElasticQueryClientException("No document found at elasticsearch with id " + id));

        log.info("Document with id {} retrieved successfully", twitterIndexModel.getId());

        return twitterIndexModel;
    }

    @Override
    public List<TwitterIndexModel> getIndexModelByText(String text) {
        List<TwitterIndexModel> searchResult = twitterElasticSearchQueryRepository.findByText(text);
        log.info("{} of documents with text {} retrieved successfully", searchResult.size(), text);
        return searchResult;
    }


    @Override
    public List<TwitterIndexModel> getAllIndexModels() {
        List<TwitterIndexModel> searchResult = CollectionsUtil.getInstance()
                .getListFromIterable(twitterElasticSearchQueryRepository.findAll());
        log.info("{} of documents retrieved successfully", searchResult.size());
        return searchResult;
    }
}
