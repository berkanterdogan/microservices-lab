package com.berkanterdogan.microservices.lab.twitter.to.kafka.service;

import com.berkanterdogan.microservices.lab.app.config.data.kafka.KafkaConfigData;
import com.berkanterdogan.microservices.lab.app.config.data.kafka.KafkaConsumerConfigData;
import com.berkanterdogan.microservices.lab.app.config.data.kafka.KafkaProducerConfigData;
import io.confluent.kafka.schemaregistry.avro.AvroSchema;
import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@TestConfiguration
@RequiredArgsConstructor
class MockSerDeConfig<K extends Serializable, V extends SpecificRecordBase> {

    private final KafkaConfigData kafkaConfigData;
    private final KafkaProducerConfigData kafkaProducerConfigData;
    private final KafkaConsumerConfigData kafkaConsumerConfigData;

    @Bean
    @Primary
    public Map<String, Object> producerConfigsForTest() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfigData.getBootstrapServers());
        props.put(kafkaConfigData.getSchemaRegistryUrlKey(), kafkaConfigData.getSchemaRegistryUrl());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaProducerConfigData.getKeySerializerClass());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaProducerConfigData.getValueSerializerClass());
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaProducerConfigData.getBatchSize() *
                kafkaProducerConfigData.getBatchSizeBoostFactor());
        props.put(ProducerConfig.LINGER_MS_CONFIG, kafkaProducerConfigData.getLingerMs());
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, kafkaProducerConfigData.getCompressionType());
        props.put(ProducerConfig.ACKS_CONFIG, kafkaProducerConfigData.getAcks());
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, kafkaProducerConfigData.getRequestTimeoutMs());
        props.put(ProducerConfig.RETRIES_CONFIG, kafkaProducerConfigData.getRetryCount());
        return props;
    }

    @Bean
    @Primary
    public Map<String, Object> consumerConfigsForTest() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfigData.getBootstrapServers());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaConsumerConfigData.getKeyDeserializer());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaConsumerConfigData.getValueDeserializer());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConsumerConfigData.getConsumerGroupId());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaConsumerConfigData.getAutoOffsetReset());
        props.put(kafkaConfigData.getSchemaRegistryUrlKey(), kafkaConfigData.getSchemaRegistryUrl());
        props.put(kafkaConsumerConfigData.getSpecificAvroReaderKey(), kafkaConsumerConfigData.getSpecificAvroReader());
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, kafkaConsumerConfigData.getSessionTimeoutMs());
        props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, kafkaConsumerConfigData.getHeartbeatIntervalMs());
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, kafkaConsumerConfigData.getMaxPollIntervalMs());
        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG,
                kafkaConsumerConfigData.getMaxPartitionFetchBytesDefault() *
                        kafkaConsumerConfigData.getMaxPartitionFetchBytesBoostFactor());
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, kafkaConsumerConfigData.getMaxPollRecords());
        return props;
    }

    @Bean
    @Primary
    public MockSchemaRegistryClient schemaRegistryClientForTest() throws IOException, RestClientException {
        MockSchemaRegistryClient mockSchemaRegistryClient = new MockSchemaRegistryClient();

        AvroSchema avroSchema = new AvroSchema("{\"type\":\"record\",\"name\":\"TwitterAvroModel\",\"namespace\":\"com.berkanterdogan.microservices.lab.kafka.model.avro\",\"fields\":[{\"name\":\"userId\",\"type\":\"long\"},{\"name\":\"id\",\"type\":\"long\"},{\"name\":\"text\",\"type\":[\"null\",{\"type\":\"string\",\"avro.java.string\":\"String\"}]},{\"name\":\"createdAt\",\"type\":[\"null\",\"long\"],\"logicalType\":[\"null\",\"date\"]}]}");
        mockSchemaRegistryClient.register("twitter-topic-value", avroSchema);

        return mockSchemaRegistryClient;
    }

    @Bean
    @Primary
    public KafkaAvroSerializer kafkaAvroSerializerForTest() throws IOException, RestClientException {
        return new KafkaAvroSerializer(schemaRegistryClientForTest());
    }

    @Bean
    @Primary
    public KafkaAvroDeserializer kafkaAvroDeserializerForTest() throws IOException, RestClientException {
        return new KafkaAvroDeserializer(schemaRegistryClientForTest(), consumerConfigsForTest());
    }

    @Bean
    @Primary
    public ProducerFactory<K, V> producerFactoryForTest() throws IOException, RestClientException {
        return new DefaultKafkaProducerFactory(
                producerConfigsForTest(),
                new LongSerializer(),
                kafkaAvroSerializerForTest()
        );
    }

    @Bean
    @Primary
    public KafkaTemplate<K, V> kafkaTemplateForTest() throws IOException, RestClientException {
        return new KafkaTemplate<>(producerFactoryForTest());
    }

    @Bean
    @Primary
    public DefaultKafkaConsumerFactory<K, V> consumerFactoryForTest() throws IOException, RestClientException {
        return new DefaultKafkaConsumerFactory(
                consumerConfigsForTest(),
                new LongDeserializer(),
                kafkaAvroDeserializerForTest()
        );
    }

    @Bean
    @Primary
    public ConcurrentKafkaListenerContainerFactory<K, V> kafkaListenerContainerFactoryForTest() throws IOException, RestClientException {
        ConcurrentKafkaListenerContainerFactory<K, V> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryForTest());
        return factory;
    }

}