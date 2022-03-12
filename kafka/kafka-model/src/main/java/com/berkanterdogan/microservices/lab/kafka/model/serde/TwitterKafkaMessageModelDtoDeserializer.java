package com.berkanterdogan.microservices.lab.kafka.model.serde;

import com.berkanterdogan.microservices.lab.kafka.model.TwitterKafkaMessageModelDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

@Slf4j
public class TwitterKafkaMessageModelDtoDeserializer implements Deserializer<TwitterKafkaMessageModelDto> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Deserializer.super.configure(configs, isKey);
    }

    @Override
    public TwitterKafkaMessageModelDto deserialize(String s, byte[] data) {
        try {
            if (data == null){
                System.out.println("Null received at deserializing");
                return null;
            }
            System.out.println("Deserializing...");
            return objectMapper.readValue(new String(data, "UTF-8"), TwitterKafkaMessageModelDto.class);
        } catch (Exception e) {
            throw new SerializationException("Error when deserializing byte[] to TwitterModelDto");
        }
    }

    @Override
    public void close() {
        Deserializer.super.close();
    }
}
