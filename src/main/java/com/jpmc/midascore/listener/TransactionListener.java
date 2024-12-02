package com.jpmc.midascore.listener;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import org.springframework.transaction.annotation.Transactional;


@Service
public class TransactionListener {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    @KafkaListener(
            topics = "${general.kafka-topic}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void listen(Transaction transaction) {
        System.out.println("Kafka Listener Invoked");

        // Validate transaction amount
        if (transaction.getAmount() <= 0) {
            System.out.println("Transaction declined: Invalid amount " + transaction.getAmount());
            return;
        }

        // Fetch sender and recipient
        UserRecord sender = userRepository.findById(transaction.getSenderId())
                .orElseThrow(() -> new IllegalArgumentException("Sender not found with ID: " + transaction.getSenderId()));
        UserRecord recipient = userRepository.findById(transaction.getRecipientId())
                .orElseThrow(() -> new IllegalArgumentException("Recipient not found with ID: " + transaction.getRecipientId()));

        // Validate sender's balance
        if (sender.getBalance() >= transaction.getAmount()) {
            // Update balances
            sender.setBalance(sender.getBalance() - transaction.getAmount());
            recipient.setBalance(recipient.getBalance() + transaction.getAmount());

            // Save updated users
            userRepository.save(sender);
            userRepository.save(recipient);

            // Record the transaction
            TransactionRecord transactionRecord = new TransactionRecord(
                    sender,
                    recipient,
                    transaction.getAmount(),
                    LocalDateTime.now()
            );
            transactionRepository.save(transactionRecord);

            System.out.println("Transaction processed: Sender ID " + sender.getId() +
                    ", Recipient ID " + recipient.getId() +
                    ", Amount " + transaction.getAmount());
        } else {
            System.out.println("Transaction declined: Insufficient balance for sender ID " + transaction.getSenderId());
        }
    }
}
