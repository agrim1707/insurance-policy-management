package com.lab.insurance.service;

import com.lab.insurance.dto.ClaimRequest;
import com.lab.insurance.exception.InvalidActionException;
import com.lab.insurance.exception.ResourceNotFoundException;
import com.lab.insurance.model.Claim;
import com.lab.insurance.model.ClaimStatus;
import com.lab.insurance.model.User;
import com.lab.insurance.model.UserPolicy;
import com.lab.insurance.repository.ClaimRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Claim submission by users and review (approve / reject) by the admin.
 */
@Service
public class ClaimService {

    private final ClaimRepository claimRepository;
    private final PolicyService policyService;

    public ClaimService(ClaimRepository claimRepository, PolicyService policyService) {
        this.claimRepository = claimRepository;
        this.policyService = policyService;
    }

    /** User submits a claim on one of his/her own purchased policies. */
    @Transactional
    public Claim submit(ClaimRequest request, User user) {
        UserPolicy userPolicy = policyService.getOwnedPolicy(request.purchasedPolicyId(), user);

        if (userPolicy.getEndDate().isBefore(LocalDate.now())) {
            throw new InvalidActionException("Policy " + userPolicy.getPolicyNumber()
                    + " has expired, claims are not allowed");
        }
        if (request.claimAmount().compareTo(userPolicy.getPolicy().getCoverageAmount()) > 0) {
            throw new InvalidActionException("Claim amount exceeds the coverage limit of "
                    + userPolicy.getPolicy().getCoverageAmount()
                    + " available on this policy");
        }
        if (request.documents().stream().distinct().count() != request.documents().size()) {
            throw new InvalidActionException("Duplicate documents in the claim");
        }

        Claim claim = new Claim();
        claim.setUserPolicy(userPolicy);
        claim.setUser(user);
        claim.setClaimAmount(request.claimAmount());
        claim.setIncidentDescription(request.incidentDescription());
        claim.setDocuments(new java.util.ArrayList<>(request.documents()));
        claim.setStatus(ClaimStatus.PENDING);
        claim.setClaimNumber(generateClaimNumber());
        return claimRepository.save(claim);
    }

    @Transactional(readOnly = true)
    public List<Claim> getMyClaims(User user) {
        return claimRepository.findByUserOrderBySubmittedAtDesc(user);
    }

    /** Admin: list all pending claims, or every claim when status is null. */
    @Transactional(readOnly = true)
    public List<Claim> listForAdmin(ClaimStatus status) {
        if (status != null) {
            return claimRepository.findByStatusOrderBySubmittedAtDesc(status);
        }
        return claimRepository.findAllByOrderBySubmittedAtDesc();
    }

    /** Admin approves a pending claim. */
    @Transactional
    public Claim approve(Long claimId, String remarks) {
        Claim claim = getForDecision(claimId);
        claim.setStatus(ClaimStatus.APPROVED);
        claim.setAdminRemarks(remarks == null || remarks.isBlank() ? "Claim approved and settled" : remarks);
        claim.setDecidedAt(LocalDateTime.now());
        return claimRepository.save(claim);
    }

    /** Admin rejects a pending claim - remarks are mandatory. */
    @Transactional
    public Claim reject(Long claimId, String remarks) {
        Claim claim = getForDecision(claimId);
        if (remarks == null || remarks.isBlank()) {
            throw new InvalidActionException("Remarks are mandatory when rejecting a claim");
        }
        claim.setStatus(ClaimStatus.REJECTED);
        claim.setAdminRemarks(remarks);
        claim.setDecidedAt(LocalDateTime.now());
        return claimRepository.save(claim);
    }

    private Claim getForDecision(Long claimId) {
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim", claimId));
        if (claim.getStatus() != ClaimStatus.PENDING) {
            throw new InvalidActionException("Claim " + claim.getClaimNumber() + " was already "
                    + claim.getStatus().name().toLowerCase() + " - a claim can be decided only once");
        }
        return claim;
    }

    private String generateClaimNumber() {
        return "IPMS-CLM-%06d".formatted(100000 + new Random().nextInt(900000));
    }
}
