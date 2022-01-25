package com.berkanterdogan.microservices.lab.twitter.to.kafka.service;

import com.berkanterdogan.microservices.lab.app.config.data.ttks.TwitterToKafkaServicePropertyConfigData;
import com.berkanterdogan.microservices.lab.twitter.to.kafka.service.init.StreamInitializer;
import com.berkanterdogan.microservices.lab.twitter.to.kafka.service.runner.TwitterStreamRunner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
@ComponentScan(basePackages = "com.berkanterdogan.microservices.lab")
public class TwitterToKafkaServiceApplication implements CommandLineRunner {

    private final TwitterToKafkaServicePropertyConfigData twitterToKafkaServicePropertyConfigData;
    private final List<TwitterStreamRunner> twitterStreamRunnerList;
    private final StreamInitializer streamInitializer;

    public static void main(String[] args) {
        SpringApplication.run(TwitterToKafkaServiceApplication.class, args);
    }

    /**
     * It is better option than the init method for application initialization logic <br/>
     * because this method has {@link String}... args parameter.
     * This method runs after the init method which is annotated with {@link PostConstruct}
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        log.info("TwitterToKafkaServiceApplication starts...");
        String welcomeMessage = this.twitterToKafkaServicePropertyConfigData.getWelcomeMessage();
        log.info(welcomeMessage);

        if (this.twitterStreamRunnerList == null) {
            log.warn("twitterStreamRunnerList is null. TwitterToKafkaServiceApplication is stopped!");
            return;
        }

        this.streamInitializer.init();
        startTwitterStreamRunners();
    }

    private void startTwitterStreamRunners() {
        for (TwitterStreamRunner twitterStreamRunner : this.twitterStreamRunnerList) {
            twitterStreamRunner.start();
        }
    }

    /**
     * It is executed after dependency injection to perform initialization
     * This method runs before the run method of {@link CommandLineRunner}
     */
    @PostConstruct
    public void init() {
        log.info("twitterToKafkaServiceApplication bean was created...");
    }
}
