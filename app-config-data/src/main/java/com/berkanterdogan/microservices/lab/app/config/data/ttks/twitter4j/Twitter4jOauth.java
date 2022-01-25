package com.berkanterdogan.microservices.lab.app.config.data.ttks.twitter4j;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Twitter4jOauth {
    private String consumerKey;
    private String consumerSecret;
    private String accessToken;
    private String accessTokenSecret;
}
