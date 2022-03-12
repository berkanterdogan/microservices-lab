package com.berkanterdogan.microservices.lab.twitter.to.kafka.service.transformer;

import com.berkanterdogan.microservices.lab.kafka.model.base.BaseKafkaMessageModelDto;

public interface KafkaNessageModelDtoTransformer<T extends BaseKafkaMessageModelDto, F> {
    T transformToModelDto(F objectToBeTransformed);
}
