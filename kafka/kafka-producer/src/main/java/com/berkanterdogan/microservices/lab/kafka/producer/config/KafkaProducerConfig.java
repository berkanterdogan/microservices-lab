package com.berkanterdogan.microservices.lab.kafka.producer.config;

import com.berkanterdogan.microservices.lab.app.config.data.kafka.KafkaConfigData;
import com.berkanterdogan.microservices.lab.app.config.data.kafka.KafkaProducerConfigData;
import com.berkanterdogan.microservices.lab.kafka.model.TwitterKafkaMessageModelDto;
import com.berkanterdogan.microservices.lab.kafka.model.serde.TwitterKafkaNessageModelDtoSeralizer;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class KafkaProducerConfig {

    private final KafkaConfigData kafkaConfigData;
    private final KafkaProducerConfigData kafkaProducerConfigData;

    @Bean
    public Map<String, Object> commonKafkaProducerConfig() {
        Map<String, Object> producerConfig = new HashMap<>();
        producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.kafkaConfigData.getBootstrapServers());
        producerConfig.put(ProducerConfig.BATCH_SIZE_CONFIG, this.kafkaProducerConfigData.getBatchSize() * this.kafkaProducerConfigData.getBatchSizeBoostFactor());
        producerConfig.put(ProducerConfig.LINGER_MS_CONFIG, this.kafkaProducerConfigData.getLingerMs());
        producerConfig.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, this.kafkaProducerConfigData.getCompressionType());
        producerConfig.put(ProducerConfig.ACKS_CONFIG, this.kafkaProducerConfigData.getAcks());
        producerConfig.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, this.kafkaProducerConfigData.getRequestTimeoutMs());
        producerConfig.put(ProducerConfig.RETRIES_CONFIG, this.kafkaProducerConfigData.getRetryCount());

        return producerConfig;
    }

    @Bean
    public ProducerFactory<Long, TwitterKafkaMessageModelDto> producerFactoryForTwitterTopic() {
        return new DefaultKafkaProducerFactory<>(commonKafkaProducerConfig(), new LongSerializer(), new TwitterKafkaNessageModelDtoSeralizer());
    }

    @Bean
    public KafkaTemplate<Long, TwitterKafkaMessageModelDto> kafkaTemplateForTwitterTopic() {
        return new KafkaTemplate<>(producerFactoryForTwitterTopic());
    }
}
