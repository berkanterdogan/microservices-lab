package com.berkanterdogan.microservices.lab.twitter.to.kafka.service.runner.impl;

import com.berkanterdogan.microservices.lab.app.config.data.ttks.TwitterToKafkaServicePropertyConfigData;
import com.berkanterdogan.microservices.lab.app.config.data.ttks.mock.MockTweetsConfig;
import com.berkanterdogan.microservices.lab.twitter.to.kafka.service.exception.TwitterToKafkaServiceException;
import com.berkanterdogan.microservices.lab.twitter.to.kafka.service.listener.TwitterToKafkaStatusListener;
import com.berkanterdogan.microservices.lab.twitter.to.kafka.service.runner.TwitterStreamRunner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "twitter-to-kafka-service.mock-tweets-config.enabled", havingValue = "true")
public class MockTweetToKafkaStreamRunner implements TwitterStreamRunner {

    private static final Random RANDOM = new Random();
    private static final String[] WORDS = new String[] {
            "Lorem",
            "ipsum",
            "dolor",
            "sit",
            "amet",
            "consectetur",
            "adipiscing",
            "elit.",
            "Phasellus",
            "eleifend",
            "consectetur",
            "elit",
            "semper",
            "odio",
            "tincidunt",
            "sit",
            "Maecenas",
            "Vivamus",
            "eleifend"
    };

    private static final String TWEET_JSON_TEMPLATE_STR = "{" +
            "\"created_at\":\"{0}\"," +
            "\"id\":\"{1}\"," +
            "\"text\":\"{2}\"," +
            "\"user\":{\"id\":\"{3}\"}" +
            "}";

    private static final String TWITTER_STATUS_DATE_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy";

    private final TwitterToKafkaServicePropertyConfigData twitterToKafkaServicePropertyConfigData;
    private final TwitterToKafkaStatusListener twitterToKafkaStatusListener;

    @Override
    public void start() {
        MockTweetsConfig mockTweetsConfig = this.twitterToKafkaServicePropertyConfigData.getMockTweetsConfig();
        Integer minTweetLength = mockTweetsConfig.getMinTweetLength();
        Integer maxTweetLength = mockTweetsConfig.getMaxTweetLength();
        Long sleepMs = mockTweetsConfig.getSleepMs();
        String[] tweetKeywords = this.twitterToKafkaServicePropertyConfigData.getTweetKeywords()
                .toArray(new String[0]);

        log.info("Filtering of mock tweet stream was started for these keywords {}.", Arrays.toString(tweetKeywords));

        simulateTwitterStream(minTweetLength, maxTweetLength, sleepMs, tweetKeywords);
    }

    private void simulateTwitterStream(Integer minTweetLength, Integer maxTweetLength, Long sleepMs, String[] tweetKeywords) {
        Executors.newSingleThreadExecutor().submit(() -> {
            while (true) {
                try {
                    String formattedTweetJsonStr = getFormattedMockTweet(tweetKeywords, minTweetLength, maxTweetLength);
                    Status status = TwitterObjectFactory.createStatus(formattedTweetJsonStr);
                    twitterToKafkaStatusListener.onStatus(status);
                    sleep(sleepMs);
                } catch (TwitterException e) {
                    log.error("An error occured while twitter status was creating!", e);
                }
            }
        });
    }

    private String getFormattedMockTweet(String[] tweetKeywords, Integer minTweetLength, Integer maxTweetLength) {
        String[] params = new String[] {
                ZonedDateTime.now().format(DateTimeFormatter.ofPattern(TWITTER_STATUS_DATE_FORMAT, Locale.ENGLISH)),
                String.valueOf(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE)),
                getRandomMockTweetContent(tweetKeywords, minTweetLength, maxTweetLength),
                String.valueOf(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE)),
        };

        return formatMockTweetAsJsonStrWithParams(params);
    }

    private String formatMockTweetAsJsonStrWithParams(String[] params) {
        String mockTweet = TWEET_JSON_TEMPLATE_STR;
        for (int i = 0; i < params.length; i++) {
            mockTweet = mockTweet.replace("{" + i + "}", params[i]);
        }
        return mockTweet;
    }

    private String getRandomMockTweetContent(String[] tweetKeywords, Integer minTweetLength, Integer maxTweetLength) {
        StringBuilder mockTweetSb = new StringBuilder();
        int mockTweetLength = RANDOM.nextInt(maxTweetLength - minTweetLength + 1) + minTweetLength;
        return constructRandomTweet(tweetKeywords, mockTweetSb, mockTweetLength);
    }

    private String constructRandomTweet(String[] tweetKeywords, StringBuilder mockTweetSb, int mockTweetLength) {
        for (int i = 0; i < mockTweetLength; i++) {
            mockTweetSb.append(WORDS[RANDOM.nextInt(WORDS.length)])
                    .append(" ");
            if (i == mockTweetLength / 2) {
                mockTweetSb.append(tweetKeywords[RANDOM.nextInt(tweetKeywords.length)])
                        .append(" ");
            }
        }

        return mockTweetSb.toString().trim();
    }

    private void sleep(Long sleepMs) {
        try {
            Thread.sleep(sleepMs);
        } catch (InterruptedException e) {
            throw new TwitterToKafkaServiceException("An error occured while sleeping for waiting new status to create!");
        }
    }


}
