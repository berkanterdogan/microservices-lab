package com.berkanterdogan.microservices.lab.twitter.to.kafka.service.init.impl;

import com.berkanterdogan.microservices.lab.app.config.data.kafka.KafkaConfigData;
import com.berkanterdogan.microservices.lab.kafka.admin.client.KafkaAdminClient;
import com.berkanterdogan.microservices.lab.twitter.to.kafka.service.init.StreamInitializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaStreamInitializer implements StreamInitializer {

    private final KafkaConfigData kafkaConfigData;
    private final KafkaAdminClient kafkaAdminClient;

    @Override
    public void init() {
        this.kafkaAdminClient.createTopics();
        this.kafkaAdminClient.checkSchemaRegistry();
        log.info("Topics with name {} is ready for operations!", this.kafkaConfigData.getTopicNamesToCreate().toArray());
    }
}
