package com.lab.insurance.controller;

import com.lab.insurance.model.User;
import com.lab.insurance.model.UserPolicy;
import com.lab.insurance.service.PolicyService;
import com.lab.insurance.service.UserService;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The purchased policies of the currently logged-in user.
 */
@RestController
@RequestMapping("/api/me/policies")
public class PolicyMeController {

    private final PolicyService policyService;
    private final UserService userService;

    public PolicyMeController(PolicyService policyService, UserService userService) {
        this.policyService = policyService;
        this.userService = userService;
    }

    @GetMapping
    public List<UserPolicy> myPolicies(Authentication authentication) {
        User user = userService.getByUsername(authentication.getName());
        return policyService.getMyPolicies(user);
    }
}
