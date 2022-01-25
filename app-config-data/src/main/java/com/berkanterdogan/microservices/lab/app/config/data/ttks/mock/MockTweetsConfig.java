package com.berkanterdogan.microservices.lab.app.config.data.ttks.mock;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MockTweetsConfig {
    private Boolean enabled;
    private Integer minTweetLength;
    private Integer maxTweetLength;
    private Long sleepMs;
}
