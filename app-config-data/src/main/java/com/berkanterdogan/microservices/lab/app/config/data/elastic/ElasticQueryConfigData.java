package com.berkanterdogan.microservices.lab.app.config.data.elastic;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "elastic-query-config")
public class ElasticQueryConfigData {

    private String textField;

}
