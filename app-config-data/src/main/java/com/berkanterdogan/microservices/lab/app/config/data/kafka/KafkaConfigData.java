package com.berkanterdogan.microservices.lab.app.config.data.kafka;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "kafka-config")
public class KafkaConfigData {

    private String bootstrapServers;
    private String schemaRegistryUrlKey;
    private String schemaRegistryUrl;
    private String topicName;
    private List<String> topicNamesToCreate;
    private Integer numOfPartitions;
    private Short replicationFactor;
}
