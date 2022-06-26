package com.berkanterdogan.microservices.lab.kafka.to.elastic.service.consumer.impl;

import com.berkanterdogan.microservices.lab.app.config.data.kafka.KafkaConfigData;
import com.berkanterdogan.microservices.lab.app.config.data.kafka.KafkaConsumerConfigData;
import com.berkanterdogan.microservices.lab.elastic.index.client.service.ElasticIndexClient;
import com.berkanterdogan.microservices.lab.elastic.model.index.impl.TwitterIndexModel;
import com.berkanterdogan.microservices.lab.kafka.admin.client.KafkaAdminClient;
import com.berkanterdogan.microservices.lab.kafka.model.avro.TwitterAvroModel;
import com.berkanterdogan.microservices.lab.kafka.to.elastic.service.consumer.KafkaConsumer;
import com.berkanterdogan.microservices.lab.kafka.to.elastic.service.transformer.ElasticModelTransformer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class TwitterKafkaConsumer implements KafkaConsumer<TwitterAvroModel> {

    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
    private final KafkaAdminClient kafkaAdminClient;
    private final KafkaConfigData kafkaConfigData;
    private final KafkaConsumerConfigData kafkaConsumerConfigData;

    private final ElasticModelTransformer<TwitterIndexModel, TwitterAvroModel> elasticModelTransformer;

    private final ElasticIndexClient<TwitterIndexModel> elasticIndexClient;

    @EventListener
    public void onAppStarted(ApplicationStartedEvent applicationStartedEvent) {
        kafkaAdminClient.checkTopicsCreated();
        log.info("Topics with name {} is ready for operations!", kafkaConfigData.getTopicNamesToCreate().toArray());
        String consumerGroupId = kafkaConsumerConfigData.getConsumerGroupId();
        Objects.requireNonNull(kafkaListenerEndpointRegistry.getListenerContainer(consumerGroupId))
                .start();
    }

    @Override
    @KafkaListener(id = "${kafka-consumer-config.consumer-group-id}", topics = "${kafka-config.topic-name}")
    public void receive(@Payload List<TwitterAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<Integer> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        log.info("{} number of messages was received with keys {}, partitions {} and offsets {}. " +
                "Sending messages to ElasticSearch: Thread id: {}",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString(),
                Thread.currentThread().getId());

        List<TwitterIndexModel> elasticModels = elasticModelTransformer.transformToElasticModel(messages);
        List<String> documentIds = elasticIndexClient.save(elasticModels);
        log.info("Documents saved to elasticsearch with ids {}", documentIds.toArray());
    }
}
