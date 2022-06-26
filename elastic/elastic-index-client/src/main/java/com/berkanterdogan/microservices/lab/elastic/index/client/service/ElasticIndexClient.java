package com.berkanterdogan.microservices.lab.elastic.index.client.service;

import com.berkanterdogan.microservices.lab.elastic.model.index.IndexModel;

import java.util.List;

public interface ElasticIndexClient<T extends IndexModel> {
    List<String> save(List<T> documents);
}
