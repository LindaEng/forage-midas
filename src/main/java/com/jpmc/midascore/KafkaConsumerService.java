package com.jpmc.midascore;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "transaction-topic", groupId = "transaction-topic")
    public void consume(String message) {
        System.out.println("Consumed message: " + message);
    }
}