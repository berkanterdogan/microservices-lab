package com.berkanterdogan.microservices.lab.elastic.index.client.service.impl;

import com.berkanterdogan.microservices.lab.app.config.data.elastic.ElasticConfigData;
import com.berkanterdogan.microservices.lab.elastic.index.client.service.ElasticIndexClient;
import com.berkanterdogan.microservices.lab.elastic.index.client.util.ElasticIndexUtil;
import com.berkanterdogan.microservices.lab.elastic.model.index.impl.TwitterIndexModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(name = "elastic-config.use-repository", havingValue = "false")
@Service
public class TwitterElasticIndexClient implements ElasticIndexClient<TwitterIndexModel> {

    private final ElasticConfigData elasticConfigData;
    private final ElasticsearchOperations elasticsearchOperations;
    private final ElasticIndexUtil<TwitterIndexModel> elasticIndexUtil;

    @Override
    public List<String> save(List<TwitterIndexModel> documents) {
        List<IndexQuery> indexQueries = elasticIndexUtil.getIndexQueries(documents);
        String indexName = elasticConfigData.getIndexName();

        List<String> documentIds = elasticsearchOperations.bulkIndex(indexQueries, IndexCoordinates.of(indexName));
        log.info("Documents indexed successfully with type {} and ids {}", TwitterIndexModel.class.getName(), documentIds);

        return documentIds;
    }
}
