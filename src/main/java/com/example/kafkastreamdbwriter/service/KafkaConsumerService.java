package com.example.kafkastreamdbwriter.service;

import com.example.kafkastreamdbwriter.model.Message;
import com.example.kafkastreamdbwriter.respository.MessageRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.RetriableException;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.SocketTimeoutException;

@Service
@Slf4j
public class KafkaConsumerService {
    private final MessageRepository messageRepository;
    private ObjectMapper objMapper = new ObjectMapper();

    public KafkaConsumerService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    // Annotation to indicate that this method listens to Kafka messages,
    // and it is retryable in case of specified exceptions.
    @RetryableTopic(
            backoff = @Backoff(delay = 2000, multiplier = 3.0),
            autoCreateTopics = "false",
            include = {SocketTimeoutException.class, RetriableException.class}
    )
    @KafkaListener(topics = "${spring.kafka.topic.name}",
            groupId = "${spring.kafka.consumer.group-id}")
    public void receiveMessage(String message,
                               @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                               @Header(KafkaHeaders.OFFSET) long offset) {
        log.info("Event message received  -> {}, topic: {}, offset: {} ", message, topic, offset);
        if (!StringUtils.hasLength(message)) {
            log.warn("Received empty message, ignoring.");
            return;
        }

        try {
            Message data = objMapper.readValue(message, Message.class);
            messageRepository.save(data);
        } catch (JsonProcessingException ex) {
            log.error("Error processing JSON message", ex);
        }
    }

    // Method to handle messages from the Dead-Letter Topic (DLT)
    @DltHandler
    public void listenDlt(String message,
                          @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                          @Header(KafkaHeaders.OFFSET) long offset) {
        log.error("DLT Received: {} , from {}, offset {} ", message, topic, offset);
    }

}
