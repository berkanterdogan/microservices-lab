package com.berkanterdogan.microservices.lab.app.config.data.ttks;

import com.berkanterdogan.microservices.lab.app.config.data.ttks.mock.MockTweetsConfig;
import com.berkanterdogan.microservices.lab.app.config.data.ttks.twitter4j.Twitter4jConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "twitter-to-kafka-service")
public class TwitterToKafkaServicePropertyConfigData {

    private String welcomeMessage;
    private MockTweetsConfig mockTweetsConfig;
    private Twitter4jConfig twitter4jConfig;
    private List<String> tweetKeywords;
}
