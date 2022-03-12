package com.berkanterdogan.microservices.lab.kafka.producer.callback;

import com.berkanterdogan.microservices.lab.kafka.model.base.BaseKafkaMessageModelDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.io.Serializable;

@Slf4j
public class KafkaListenableFutureCallback<K extends Serializable, V extends BaseKafkaMessageModelDto> implements ListenableFutureCallback<SendResult<K, V>> {

    private final String topicName;
    private final K key;
    private final V message;

    public KafkaListenableFutureCallback(String topicName, K key, V message) {
        this.topicName = topicName;
        this.key = key;
        this.message = message;
    }

    @Override
    public void onFailure(Throwable ex) {
        log.error("Error was occured while sending message {} with key {} to topic {}", message.toString(), key, topicName, ex);
    }

    @Override
    public void onSuccess(SendResult<K, V> result) {
        RecordMetadata recordMetadata = result.getRecordMetadata();
        log.debug("Received new metadata, Topic: {}, Partition {}, Offset {}, Timestamp {}, at time {}",
                recordMetadata.topic(),
                recordMetadata.partition(),
                recordMetadata.offset(),
                recordMetadata.timestamp(),
                System.nanoTime());
    }
}
