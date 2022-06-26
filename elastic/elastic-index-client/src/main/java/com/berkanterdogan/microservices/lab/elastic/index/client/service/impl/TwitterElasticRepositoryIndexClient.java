package com.berkanterdogan.microservices.lab.elastic.index.client.service.impl;

import com.berkanterdogan.microservices.lab.elastic.index.client.repository.TwitterElasticsearchRepository;
import com.berkanterdogan.microservices.lab.elastic.index.client.service.ElasticIndexClient;
import com.berkanterdogan.microservices.lab.elastic.model.index.impl.TwitterIndexModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(name = "elastic-config.use-repository", havingValue = "true", matchIfMissing = true)
@Service
public class TwitterElasticRepositoryIndexClient implements ElasticIndexClient<TwitterIndexModel>{

    private final TwitterElasticsearchRepository twitterElasticsearchRepository;

    @Override
    public List<String> save(List<TwitterIndexModel> documents) {
        List<TwitterIndexModel> repositoryResponse = (List<TwitterIndexModel>)twitterElasticsearchRepository.saveAll(documents);
        List<String> documentIds = repositoryResponse.stream().map(TwitterIndexModel::getId).collect(Collectors.toList());

        log.info("Documents indexed successfully with type {} and ids {}", TwitterIndexModel.class.getName(), documentIds);

        return documentIds;
    }
}
