package com.berkanterdogan.microservices.lab.twitter.to.kafka.service;

import com.berkanterdogan.microservices.lab.app.config.data.kafka.KafkaConfigData;
import com.berkanterdogan.microservices.lab.app.config.data.kafka.KafkaConsumerConfigData;
import com.berkanterdogan.microservices.lab.app.config.data.kafka.KafkaProducerConfigData;
import com.berkanterdogan.microservices.lab.kafka.model.TwitterKafkaMessageModelDto;
import com.berkanterdogan.microservices.lab.kafka.model.base.BaseKafkaMessageModelDto;
import com.berkanterdogan.microservices.lab.kafka.model.serde.TwitterKafkaMessageModelDtoDeserializer;
import com.berkanterdogan.microservices.lab.kafka.model.serde.TwitterKafkaNessageModelDtoSeralizer;
import lombok.RequiredArgsConstructor;
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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@TestConfiguration
@RequiredArgsConstructor
class MockSerDeConfig<K extends Serializable, V extends BaseKafkaMessageModelDto> {

    private final KafkaConfigData kafkaConfigData;
    private final KafkaProducerConfigData kafkaProducerConfigData;
    private final KafkaConsumerConfigData kafkaConsumerConfigData;

    @Bean
    @Primary
    public Map<String, Object> testBeanCommonKafkaProducerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfigData.getBootstrapServers());
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
    public Map<String, Object> testBeanCommonKafkaConsumerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfigData.getBootstrapServers());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaConsumerConfigData.getAutoOffsetReset());
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
    public Map<String, Object> testBeanKafkaConsumerConfigForTwitterTopic() {
        Map<String, Object> props = testBeanCommonKafkaConsumerConfig();
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConsumerConfigData.getTwitterTopicConsumerGroupId());
        return props;
    }

    @Bean
    @Primary
    public ProducerFactory<Long, TwitterKafkaMessageModelDto> testBeanProducerFactoryForTwitterTopic() {
        return new DefaultKafkaProducerFactory<>(
                testBeanCommonKafkaProducerConfig(),
                new LongSerializer(),
                new TwitterKafkaNessageModelDtoSeralizer()
        );
    }

    @Bean
    @Primary
    public KafkaTemplate<Long, TwitterKafkaMessageModelDto> testBeanKafkaTemplateForTwitterTopic() {
        return new KafkaTemplate<>(testBeanProducerFactoryForTwitterTopic());
    }

    @Bean
    @Primary
    public DefaultKafkaConsumerFactory<Long, TwitterKafkaMessageModelDto> testBeanConsumerFactoryForTwitterTopic() {
        return new DefaultKafkaConsumerFactory<>(
                testBeanKafkaConsumerConfigForTwitterTopic(),
                new LongDeserializer(),
                new TwitterKafkaMessageModelDtoDeserializer()
        );
    }

    @Bean
    @Primary
    public ConcurrentKafkaListenerContainerFactory<Long, TwitterKafkaMessageModelDto> testBeanKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Long, TwitterKafkaMessageModelDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(testBeanConsumerFactoryForTwitterTopic());
        return factory;
    }

}