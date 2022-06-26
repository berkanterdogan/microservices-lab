package com.berkanterdogan.microservices.lab.elastic.index.client.repository;

import com.berkanterdogan.microservices.lab.elastic.model.index.impl.TwitterIndexModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TwitterElasticsearchRepository extends ElasticsearchRepository<TwitterIndexModel, String> {
}
