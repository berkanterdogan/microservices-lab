package com.berkanterdogan.microservices.lab.twitter.to.kafka.service.runner.impl;

import com.berkanterdogan.microservices.lab.app.config.data.ttks.TwitterToKafkaServicePropertyConfigData;
import com.berkanterdogan.microservices.lab.twitter.to.kafka.service.listener.TwitterToKafkaStatusListener;
import com.berkanterdogan.microservices.lab.twitter.to.kafka.service.runner.TwitterStreamRunner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import twitter4j.FilterQuery;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

import javax.annotation.PreDestroy;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
@Component
@ConditionalOnProperty(name = "twitter-to-kafka-service.twitter4j-config.enabled", havingValue = "true")
public class TwitterToKafkaStreamRunner implements TwitterStreamRunner {

    private final TwitterToKafkaServicePropertyConfigData twitterToKafkaServicePropertyConfigData;
    private final TwitterStreamFactory twitterStreamFactory;
    private final TwitterToKafkaStatusListener twitterToKafkaStatusListener;

    private TwitterStream twitterStream;

    @Override
    public void start() {
        this.twitterStream = this.twitterStreamFactory.getInstance();
        this.twitterStream.addListener(this.twitterToKafkaStatusListener);
        addFilter();
    }

    private void addFilter() {
        String[] twitterKeywords = this.twitterToKafkaServicePropertyConfigData.getTweetKeywords()
                .toArray(new String[0]);
        FilterQuery filterQuery = new FilterQuery(twitterKeywords);
        this.twitterStream.filter(filterQuery);
        log.info("Filtering of twitter stream was started for these keywords {}.", Arrays.toString(twitterKeywords));
    }

    @PreDestroy
    public void shutdown() {
        if (this.twitterStream != null) {
            this.twitterStream.shutdown();
            log.info("Twitter stream was closed.");
        }
    }
}
