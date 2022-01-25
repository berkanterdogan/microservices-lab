package com.berkanterdogan.microservices.lab.twitter.to.kafka.service.config.property.validation;

import com.berkanterdogan.microservices.lab.app.config.data.ttks.twitter4j.Twitter4jOauth;
import com.berkanterdogan.microservices.lab.twitter.to.kafka.service.exception.TwitterToKafkaServiceException;
import org.springframework.stereotype.Component;

@Component
public class Twitter4jOauthConfigDataValidation {

    public void validate(Twitter4jOauth twitter4jOauth) {

        StringBuilder validationMessageSb = new StringBuilder("There are lacks for the twitter4j oauth configurations: ");
        int lengthBeforeValidation = validationMessageSb.length();

        String consumerKey = twitter4jOauth.getConsumerKey();
        String consumerSecret = twitter4jOauth.getConsumerSecret();
        String accessToken = twitter4jOauth.getAccessToken();
        String accessTokenSecret = twitter4jOauth.getAccessTokenSecret();

        checkProperty("TWITTER4J_OAUTH_CONSUMER_KEY", consumerKey, validationMessageSb);
        checkProperty("TWITTER4J_OAUTH_CONSUMER_SECRET", consumerSecret, validationMessageSb);
        checkProperty("TWITTER4J_OAUTH_ACCESS_TOKEN_SECRET", accessToken, validationMessageSb);
        checkProperty("TWITTER4J_OAUTH_ACCESS_TOKEN", accessTokenSecret, validationMessageSb);

        completeValidation(validationMessageSb, lengthBeforeValidation);
    }


    private void checkProperty(String environmetVariableName, String propertyValue, StringBuilder validationMessageSb) {
        if (propertyValue.isBlank() || propertyValue.startsWith("${")) {
            appendLackDetail(environmetVariableName, validationMessageSb);
        }
    }

    private void appendLackDetail(String environmetVariableName, StringBuilder validationMessageSb) {
        validationMessageSb.append(environmetVariableName);
        validationMessageSb.append(" environment variable was not set. ");
    }

    private void completeValidation(StringBuilder validationMessageSb, int lengthBeforeValidation) {
        int lengthAfterValidation = validationMessageSb.length();
        if (lengthAfterValidation > lengthBeforeValidation)
            throw new TwitterToKafkaServiceException(validationMessageSb.toString());
    }

}
