package com.lab.insurance.security;

import com.lab.insurance.model.Role;
import com.lab.insurance.model.User;
import com.lab.insurance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Seeds one ADMIN account and one demo USER account so the app can be tested
 * immediately after startup:
 *
 *   admin / admin@insurance.com           password: Admin@123   (Role ADMIN)
 *   john  / john.doe@example.com          password: John@123    (Role USER)
 */
@Component
public class AdminSeed implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (userRepository.findByUsername("admin").isEmpty()) {
            userRepository.save(new User("admin", "admin@insurance.com", "System Admin",
                    passwordEncoder.encode("Admin@123"), Role.ADMIN));
        }
        if (userRepository.findByUsername("john").isEmpty()) {
            userRepository.save(new User("john", "john.doe@example.com", "John Doe",
                    passwordEncoder.encode("John@123"), Role.USER));
        }
    }
}
