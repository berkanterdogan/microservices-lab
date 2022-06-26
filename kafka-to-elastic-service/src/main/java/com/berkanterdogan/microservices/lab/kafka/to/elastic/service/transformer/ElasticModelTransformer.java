package com.berkanterdogan.microservices.lab.kafka.to.elastic.service.transformer;

import com.berkanterdogan.microservices.lab.elastic.model.index.IndexModel;
import org.apache.avro.specific.SpecificRecordBase;

import java.util.List;

public interface ElasticModelTransformer<T extends IndexModel, F extends SpecificRecordBase> {
    List<T> transformToElasticModel(List<F> objectsToBeTransformed);
}
