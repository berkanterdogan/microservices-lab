package com.berkanterdogan.microservices.lab.app.config.data.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "user-config")
public class UserConfigData {
    private String username;
    private String password;
    private String[] roles;
}
