package com.berkanterdogan.microservices.lab.kafka.admin.client;

import org.apache.kafka.clients.admin.TopicListing;

import java.util.Collection;

public interface KafkaAdminClient {
    void createTopics();

    void checkTopicsCreated();

    void checkSchemaRegistry();

    Collection<TopicListing> getTopics();
}
