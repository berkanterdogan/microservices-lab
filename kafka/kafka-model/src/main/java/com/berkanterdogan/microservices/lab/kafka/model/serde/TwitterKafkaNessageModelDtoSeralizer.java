package com.berkanterdogan.microservices.lab.kafka.model.serde;

import com.berkanterdogan.microservices.lab.kafka.model.TwitterKafkaMessageModelDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

@Slf4j
public class TwitterKafkaNessageModelDtoSeralizer implements Serializer<TwitterKafkaMessageModelDto> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String s, TwitterKafkaMessageModelDto twitterKafkaMessageModelDto) {
        try {
            if (twitterKafkaMessageModelDto == null){
                log.warn("Null received at serializing");
                return null;
            }
            log.info("Serializing...");
            return objectMapper.writeValueAsBytes(twitterKafkaMessageModelDto);
        } catch (Exception e) {
            throw new SerializationException("Error when serializing TwitterModelDto to byte[]");
        }    }

    @Override
    public void close() {
    }
}
