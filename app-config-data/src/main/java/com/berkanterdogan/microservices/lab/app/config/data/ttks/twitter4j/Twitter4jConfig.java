package com.berkanterdogan.microservices.lab.app.config.data.ttks.twitter4j;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Twitter4jConfig {
    private Boolean enabled;
    private Boolean debug;
    private Twitter4jOauth oauth;

}
