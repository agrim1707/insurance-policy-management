package com.lab.insurance.service;

import com.lab.insurance.exception.InvalidActionException;
import com.lab.insurance.exception.ResourceNotFoundException;
import com.lab.insurance.model.Policy;
import com.lab.insurance.model.PolicyCategory;
import com.lab.insurance.model.User;
import com.lab.insurance.model.UserPolicy;
import com.lab.insurance.repository.PolicyRepository;
import com.lab.insurance.repository.UserPolicyRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Browsing the available policy plans and purchasing a policy.
 */
@Service
public class PolicyService {

    private final PolicyRepository policyRepository;
    private final UserPolicyRepository userPolicyRepository;

    public PolicyService(PolicyRepository policyRepository, UserPolicyRepository userPolicyRepository) {
        this.policyRepository = policyRepository;
        this.userPolicyRepository = userPolicyRepository;
    }

    /** List all active plans; optionally filter by category (e.g. HEALTH). */
    @Transactional(readOnly = true)
    public List<Policy> browse(String category) {
        if (category != null && !category.isBlank()) {
            PolicyCategory cat;
            try {
                cat = PolicyCategory.valueOf(category.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new InvalidActionException(
                        "Unknown category '" + category + "'. Use one of: LIFE, HEALTH, VEHICLE, TRAVEL, HOME");
            }
            return policyRepository.findByCategoryAndActiveTrue(cat);
        }
        return policyRepository.findByActiveTrue();
    }

    @Transactional(readOnly = true)
    public Policy getPlan(Long id) {
        return policyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Policy plan", id));
    }

    /** Purchase a plan for a user: creates a UserPolicy with generated number and dates. */
    @Transactional
    public UserPolicy purchase(Long planId, User user) {
        Policy plan = getPlan(planId);
        if (!plan.isActive()) {
            throw new InvalidActionException("This policy plan is not available for sale");
        }
        UserPolicy up = new UserPolicy();
        up.setPolicy(plan);
        up.setUser(user);
        up.setPremiumPaid(plan.getPremiumAmount());
        up.setPurchaseDate(LocalDate.now());
        up.setStartDate(LocalDate.now());
        up.setEndDate(LocalDate.now().plusMonths(plan.getTenureMonths()));
        up.setPolicyNumber(generatePolicyNumber());
        return userPolicyRepository.save(up);
    }

    @Transactional(readOnly = true)
    public List<UserPolicy> getMyPolicies(User user) {
        return userPolicyRepository.findByUser(user);
    }

    @Transactional(readOnly = true)
    public UserPolicy getOwnedPolicy(Long userPolicyId, User user) {
        UserPolicy up = userPolicyRepository.findById(userPolicyId)
                .orElseThrow(() -> new ResourceNotFoundException("Purchased policy", userPolicyId));
        if (!up.getUser().getId().equals(user.getId())) {
            throw new InvalidActionException("You can only raise claims on your own policies");
        }
        return up;
    }

    private String generatePolicyNumber() {
        return "IPMS-POL-%06d".formatted(100000 + new Random().nextInt(900000));
    }
}
