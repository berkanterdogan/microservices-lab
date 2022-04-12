package com.berkanterdogan.microservices.lab.twitter.to.kafka.service.transformer;

import org.apache.avro.specific.SpecificRecordBase;

public interface AvroTransformer<T extends SpecificRecordBase, F> {
    T transformToAvroModel(F objectToBeTransformed);
}
