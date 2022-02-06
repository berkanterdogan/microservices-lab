package com.berkanterdogan.microservices.lab.kafka.admin.client;

import com.berkanterdogan.microservices.lab.kafka.admin.exception.KafkaAdminClientException;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.retry.RetryContext;
import org.springframework.web.reactive.function.client.ClientResponse;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author berkant
 * @since 3.02.2022
 */
public interface KafkaAdminClient {
    void createTopics();

    void checkTopicsCreated();

    void checkSchemaRegistry();

    Collection<TopicListing> getTopics();
}
