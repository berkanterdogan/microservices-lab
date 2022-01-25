package com.berkanterdogan.microservices.lab.common.config.retry;

import com.berkanterdogan.microservices.lab.app.config.data.retry.RetryConfigData;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@RequiredArgsConstructor
@Configuration
public class RetryConfig {

    private final RetryConfigData retryConfigData;

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        ExponentialBackOffPolicy exponentialBackOffPolicy = new ExponentialBackOffPolicy();
        exponentialBackOffPolicy.setInitialInterval(this.retryConfigData.getInitialIntervalMs());
        exponentialBackOffPolicy.setMaxInterval(this.retryConfigData.getMaxIntervalMs());
        exponentialBackOffPolicy.setMultiplier(this.retryConfigData.getMultiplier());

        retryTemplate.setBackOffPolicy(exponentialBackOffPolicy);

        SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy();
        simpleRetryPolicy.setMaxAttempts(this.retryConfigData.getMaxAttempts());

        retryTemplate.setRetryPolicy(simpleRetryPolicy);

        return retryTemplate;
    }
}
