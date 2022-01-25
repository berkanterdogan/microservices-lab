package com.berkanterdogan.microservices.lab.kafka.admin.client;

import com.berkanterdogan.microservices.lab.app.config.data.kafka.KafkaConfigData;
import com.berkanterdogan.microservices.lab.app.config.data.retry.RetryConfigData;
import com.berkanterdogan.microservices.lab.kafka.admin.exception.KafkaAdminClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaAdminClient {

    private final KafkaConfigData kafkaConfigData;
    private final RetryConfigData retryConfigData;
    private final AdminClient adminClient;
    private final RetryTemplate retryTemplate;
    private final WebClient webClient;

    public void createTopics() {
        CreateTopicsResult createTopicsResult;
        try {
            createTopicsResult = this.retryTemplate.execute(this::doCreateTopics);
            log.info("Create topic result {}", createTopicsResult.values().values());
        } catch (Throwable t) {
            throw new KafkaAdminClientException("Reached max number of retry for creating Kafka topic(s)!", t);
        }

        checkTopicsCreated();
    }

    public void checkTopicsCreated() {
        Collection<TopicListing> topics = getTopics();
        int retryCount = 1;
        Integer maxRetry = this.retryConfigData.getMaxAttempts();
        Integer multiplier = this.retryConfigData.getMultiplier().intValue();
        Long sleepTimeMs = this.retryConfigData.getSleepTimeMs();
        List<String> topicNames = this.kafkaConfigData.getTopicNamesToCreate();
        for (String topicName : topicNames) {
            // custom retry for checking if topic created
            while (!isTopicCreated(topics, topicName)) {
                checkMaxRetry(retryCount++, maxRetry, "Reached max number of retry for checking if topic " + topicName + " created!");
                sleep(sleepTimeMs);
                sleepTimeMs *= multiplier;
                topics = getTopics();
            }
        }
    }

    public void checkSchemaRegistry() {
        int retryCount = 1;
        Integer maxRetry = this.retryConfigData.getMaxAttempts();
        Integer multiplier = this.retryConfigData.getMultiplier().intValue();
        Long sleepTimeMs = this.retryConfigData.getSleepTimeMs();
        while (!getSchemaRegistryStatus().is2xxSuccessful()) {
            checkMaxRetry(retryCount++, maxRetry, "Reached max number of retry for checking if schema registry is up!");
            sleep(sleepTimeMs);
            sleepTimeMs *= multiplier;
        }
    }

    private HttpStatus getSchemaRegistryStatus() {
        HttpStatus result;
        try {
            result = this.webClient.method(HttpMethod.GET)
                    .uri(this.kafkaConfigData.getSchemaRegistryUrl())
                    .exchange()
                    .map(ClientResponse::statusCode)
                    .block();
        } catch (Exception e) {
            result = HttpStatus.SERVICE_UNAVAILABLE;
        }

        return result;
    }

    private void sleep(Long sleepTimeMs) {
        try {
            Thread.sleep(sleepTimeMs);
        } catch (InterruptedException e) {
            throw new KafkaAdminClientException("Error while sleeping for waiting new created topic(s)!");
        }
    }

    private void checkMaxRetry(int retryCount, Integer maxRetry, String exceptionMessage) {
        if (retryCount > maxRetry) {
            throw new KafkaAdminClientException(exceptionMessage);
        }
    }

    private boolean isTopicCreated(Collection<TopicListing> topics, String topicName) {
        if (topics == null) {
            return false;
        }

        return topics.stream().anyMatch(topic -> topic.name().equals(topicName));
    }

    private CreateTopicsResult doCreateTopics(RetryContext retryContext) {
        List<String> topicNames = this.kafkaConfigData.getTopicNamesToCreate();
        log.info("Creating {} kafka topic(s), attempt {}", topicNames.size(), retryContext.getRetryCount());

        List<NewTopic> kafkaTopics = topicNames.stream().map(topicName -> new NewTopic(
                topicName.trim(),
                kafkaConfigData.getNumOfPartitions(),
                kafkaConfigData.getReplicationFactor()
        )).collect(Collectors.toList());

        return this.adminClient.createTopics(kafkaTopics);
    }

    public Collection<TopicListing> getTopics() {
        Collection<TopicListing> topics;
        try {
            topics = this.retryTemplate.execute(this::doGetTopics);
        } catch (Throwable t) {
            throw new KafkaAdminClientException("Reached max number of retry for reading Kafka topic(s)!", t);
        }

        return topics;
    }

    private Collection<TopicListing> doGetTopics(RetryContext retryContext) throws ExecutionException, InterruptedException {
        List<String> topicNames = this.kafkaConfigData.getTopicNamesToCreate();
        log.info("Reading {} kafka topic(s), attempt {}", topicNames.toArray(), retryContext.getRetryCount());

        Collection<TopicListing> topics = this.adminClient.listTopics().listings().get();
        if (topics != null) {
            topics.forEach(topic -> log.debug("Topic with name {}", topic.name()));
        }

        return topics;
    }

}
