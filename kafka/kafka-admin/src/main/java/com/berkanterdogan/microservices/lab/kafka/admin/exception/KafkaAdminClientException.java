package com.berkanterdogan.microservices.lab.kafka.admin.exception;

/**
 * Exception class for Kafka client error situations
 */
public class KafkaAdminClientException extends RuntimeException {

    public KafkaAdminClientException() {
    }

    public KafkaAdminClientException(String message) {
        super(message);
    }

    public KafkaAdminClientException(String message, Throwable cause) {
        super(message, cause);
    }


}
