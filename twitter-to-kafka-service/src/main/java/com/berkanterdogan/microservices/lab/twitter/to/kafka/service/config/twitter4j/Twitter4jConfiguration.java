package com.berkanterdogan.microservices.lab.twitter.to.kafka.service.config.twitter4j;

import com.berkanterdogan.microservices.lab.app.config.data.ttks.TwitterToKafkaServicePropertyConfigData;
import com.berkanterdogan.microservices.lab.app.config.data.ttks.twitter4j.Twitter4jConfig;
import com.berkanterdogan.microservices.lab.app.config.data.ttks.twitter4j.Twitter4jOauth;
import com.berkanterdogan.microservices.lab.twitter.to.kafka.service.config.property.validation.Twitter4jOauthConfigDataValidation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

@Configuration
public class Twitter4jConfiguration {

    private final TwitterToKafkaServicePropertyConfigData twitterToKafkaServicePropertyConfigData;
    private final Twitter4jOauthConfigDataValidation twitter4jOauthConfigDataValidation;

    public Twitter4jConfiguration(TwitterToKafkaServicePropertyConfigData twitterToKafkaServicePropertyConfigData, Twitter4jOauthConfigDataValidation twitter4jOauthConfigDataValidation) {
        this.twitterToKafkaServicePropertyConfigData = twitterToKafkaServicePropertyConfigData;
        this.twitter4jOauthConfigDataValidation = twitter4jOauthConfigDataValidation;
    }

    @Bean
    @ConditionalOnProperty(name = "twitter-to-kafka-service.twitter4j-config.enabled", havingValue = "true")
    public TwitterStreamFactory twitterStreamFactory() {
        ConfigurationBuilder configurationBuilder = twitterConfigurationBuilder();

        return new TwitterStreamFactory(configurationBuilder.build());
    }

    @Bean
    @ConditionalOnProperty(name = "twitter-to-kafka-service.twitter4j-config.enabled", havingValue = "true")
    public ConfigurationBuilder twitterConfigurationBuilder() {
        Twitter4jConfig twitter4jConfig = this.twitterToKafkaServicePropertyConfigData.getTwitter4jConfig();

        Twitter4jOauth twitter4jOauth = twitter4jConfig.getOauth();

        this.twitter4jOauthConfigDataValidation.validate(twitter4jOauth);
        Boolean debug = twitter4jConfig.getDebug();
        String consumerKey = twitter4jOauth.getConsumerKey();
        String consumerSecret = twitter4jOauth.getConsumerSecret();
        String accessToken = twitter4jOauth.getAccessToken();
        String accessTokenSecret = twitter4jOauth.getAccessTokenSecret();

        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setDebugEnabled(debug)
                .setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerSecret)
                .setOAuthAccessToken(accessToken)
                .setOAuthAccessTokenSecret(accessTokenSecret);

        return configurationBuilder;
    }
}
