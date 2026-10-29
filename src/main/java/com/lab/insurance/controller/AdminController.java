package com.lab.insurance.controller;

import com.lab.insurance.dto.ClaimDecisionRequest;
import com.lab.insurance.dto.MessageResponse;
import com.lab.insurance.model.Claim;
import com.lab.insurance.model.ClaimStatus;
import com.lab.insurance.service.ClaimService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Admin-only endpoints: review claims and approve / reject them.
 * Single sign-in: admin / Admin@123 (role ADMIN).
 */
@RestController
@RequestMapping("/api/admin/claims")
public class AdminController {

    private final ClaimService claimService;

    public AdminController(ClaimService claimService) {
        this.claimService = claimService;
    }

    /** List claims; ?status=PENDING (default view for review) or omit for all. */
    @GetMapping
    public List<Claim> list(@RequestParam(required = false) ClaimStatus status) {
        return claimService.listForAdmin(status);
    }

    /** Approve a pending claim (optional remarks). */
    @PutMapping("/{id}/approve")
    public Claim approve(@PathVariable Long id, @Valid @RequestBody(required = false) ClaimDecisionRequest request) {
        return claimService.approve(id, request == null ? null : request.remarks());
    }

    /** Reject a pending claim (remarks mandatory). */
    @PutMapping("/{id}/reject")
    public Claim reject(@PathVariable Long id, @Valid @RequestBody ClaimDecisionRequest request) {
        return claimService.reject(id, request == null ? null : request.remarks());
    }
}
