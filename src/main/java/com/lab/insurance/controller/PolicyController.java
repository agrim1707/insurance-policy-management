package com.lab.insurance.controller;

import com.lab.insurance.model.Policy;
import com.lab.insurance.model.User;
import com.lab.insurance.model.UserPolicy;
import com.lab.insurance.service.PolicyService;
import com.lab.insurance.service.UserService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Policy browsing (public) and purchase (USER only).
 */
@RestController
@RequestMapping("/api/policies")
public class PolicyController {

    private final PolicyService policyService;
    private final UserService userService;

    public PolicyController(PolicyService policyService, UserService userService) {
        this.policyService = policyService;
        this.userService = userService;
    }

    /** Public - browse all available plans, optionally ?category=HEALTH */
    @GetMapping
    public List<Policy> browse(@RequestParam(required = false) String category) {
        return policyService.browse(category);
    }

    /** Public - view one plan. */
    @GetMapping("/{id}")
    public Policy getPlan(@PathVariable Long id) {
        return policyService.getPlan(id);
    }

    /** USER only - purchase (subscribe to) a plan. */
    @PostMapping("/{id}/purchase")
    public UserPolicy purchase(@PathVariable Long id, org.springframework.security.core.Authentication authentication) {
        User user = userService.getByUsername(authentication.getName());
        return policyService.purchase(id, user);
    }
}
