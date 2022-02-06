package com.berkanterdogan.microservices.lab.twitter.to.kafka.service.init.impl;

import com.berkanterdogan.microservices.lab.app.config.data.kafka.KafkaConfigData;
import com.berkanterdogan.microservices.lab.kafka.admin.client.KafkaAdminClient;
import com.berkanterdogan.microservices.lab.twitter.to.kafka.service.init.StreamInitializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaStreamInitializer implements StreamInitializer {
    private final Environment environment;
    private final KafkaConfigData kafkaConfigData;
    private final KafkaAdminClient kafkaAdminClientImpl;

    @Override
    public void init() {
        log.info("Active profiles => " + Arrays.toString(environment.getActiveProfiles()));
        this.kafkaAdminClientImpl.createTopics();
        if (!List.of(environment.getActiveProfiles()).contains("test")) {
            this.kafkaAdminClientImpl.checkSchemaRegistry();
        }
        log.info("Topics with name {} is ready for operations!", this.kafkaConfigData.getTopicNamesToCreate().toArray());
    }
}
