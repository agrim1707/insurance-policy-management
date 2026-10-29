package com.lab.insurance.controller;

import com.lab.insurance.dto.RegisterRequest;
import com.lab.insurance.dto.MessageResponse;
import com.lab.insurance.model.User;
import com.lab.insurance.service.UserService;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Registration (public) and current-user profile endpoints.
 */
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /** Public - anyone can create a customer account. */
    @PostMapping("/auth/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody RegisterRequest request) {
        User user = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Registration successful. Login with your username and password.",
                "username", user.getUsername(),
                "email", user.getEmail(),
                "role", user.getRole().name()));
    }

    /** Who am I (after Basic auth login). */
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me(Authentication authentication) {
        User user = userService.getByUsername(authentication.getName());
        return ResponseEntity.ok(Map.of(
                "username", user.getUsername(),
                "fullName", user.getFullName(),
                "email", user.getEmail(),
                "role", user.getRole().name()));
    }

    /** Simple logout info for REST clients (stateless - simply stop sending credentials). */
    @PostMapping("/auth/logout")
    public ResponseEntity<MessageResponse> logout() {
        return ResponseEntity.ok(new MessageResponse(
                "Session is stateless - remove the Authorization header to log out."));
    }
}
