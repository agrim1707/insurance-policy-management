package com.lab.insurance.service;

import com.lab.insurance.dto.RegisterRequest;
import com.lab.insurance.exception.InvalidActionException;
import com.lab.insurance.model.Role;
import com.lab.insurance.model.User;
import com.lab.insurance.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Handles user registration. Passwords are never stored in plain text -
 * they are hashed with BCrypt (a Spring Security bean).
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new InvalidActionException("Username '" + request.username() + "' is already taken");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new InvalidActionException("Email '" + request.email() + "' is already registered");
        }
        User user = new User(
                request.username(),
                request.email(),
                request.fullName(),
                passwordEncoder.encode(request.password()),
                Role.USER);
        return userRepository.save(user);
    }

    /** Look up the user by authenticated username, used by controllers. */
    @Transactional(readOnly = true)
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidActionException("Authenticated user not found: " + username));
    }
}
