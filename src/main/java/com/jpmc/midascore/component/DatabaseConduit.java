package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConduit {
    private final UserRepository userRepository;

    public DatabaseConduit(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(UserRecord userRecord) {

        System.out.println("Attempting to save User: " + userRecord);
            userRepository.save(userRecord); // Ensure this line is actually saving
            System.out.println("User saved successfully: " + userRecord);
        userRepository.save(userRecord);
    }

}
