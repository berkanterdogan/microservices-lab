package com.berkanterdogan.microservices.lab.elastic.query.service.business;

import com.berkanterdogan.microservices.lab.elastic.query.service.model.ElasticQueryServiceResponseModel;

import java.util.List;

public interface ElasticQueryService {
    List<ElasticQueryServiceResponseModel> getAllDocuments();
    ElasticQueryServiceResponseModel getDocumentById(String id);
    List<ElasticQueryServiceResponseModel> getDocumentsByText(String text);
}
