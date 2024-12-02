package com.jpmc.midascore.listener;

import com.jpmc.midascore.foundation.Transaction;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TransactionListener {

    // KafkaListener listens to the topic and automatically deserializes the message into a Transaction object
    @KafkaListener(
            topics = "${general.kafka-topic}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void listen(Transaction transaction) {
        System.out.println("Kafka Listener Invoked");
        if (transaction != null) {
            System.out.println("Received transaction: " + transaction);
        } else {
            System.out.println("No transaction received.");
        }
    }
}
