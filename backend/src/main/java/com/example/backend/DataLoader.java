package com.example.backend;

import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@org.springframework.context.annotation.Configuration
public class DataLoader {
    @Bean
    CommandLineRunner loadData(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        return args -> {
            if (userRepository.findByUsername("admin") == null) {
                userRepository.save(new User("admin", encoder.encode("admin123"), "ROLE_ADMIN"));
                userRepository.save(new User("user", encoder.encode("user123"), "ROLE_USER"));
            }
        };
    }
}
