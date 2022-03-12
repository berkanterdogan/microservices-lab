package com.berkanterdogan.microservices.lab.twitter.to.kafka.service;

import com.berkanterdogan.microservices.lab.kafka.model.TwitterKafkaMessageModelDto;
import com.berkanterdogan.microservices.lab.kafka.producer.service.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
@Import(MockSerDeConfig.class)
@RequiredArgsConstructor(onConstructor_={@Autowired})
public class TwitterToKafkaServiceApplicationTests {

    private final KafkaProducer<Long, TwitterKafkaMessageModelDto> kafkaProducer;

    @Test
    public void testMessageProducementToKafka() throws InterruptedException {
        TwitterKafkaMessageModelDto twitterKafkaMessageModelDto = new TwitterKafkaMessageModelDto();
        twitterKafkaMessageModelDto.setId(12345L);
        twitterKafkaMessageModelDto.setUserId(1234L);
        twitterKafkaMessageModelDto.setCreatedAt(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        twitterKafkaMessageModelDto.setText("TEST");
        kafkaProducer.send("twitter-topic", twitterKafkaMessageModelDto.getUserId(), twitterKafkaMessageModelDto);
        Thread.sleep(20000L);
    }
}