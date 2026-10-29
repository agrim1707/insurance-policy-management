package com.lab.insurance.controller;

import com.lab.insurance.dto.ClaimRequest;
import com.lab.insurance.model.Claim;
import com.lab.insurance.model.User;
import com.lab.insurance.service.ClaimService;
import com.lab.insurance.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Claim submission and claim tracking for the logged-in customer.
 */
@RestController
@RequestMapping("/api")
public class ClaimController {

    private final ClaimService claimService;
    private final UserService userService;

    public ClaimController(ClaimService claimService, UserService userService) {
        this.claimService = claimService;
        this.userService = userService;
    }

    /** Submit a claim with required details + documents (USER only). */
    @PostMapping("/claims")
    public ResponseEntity<Claim> submit(@Valid @RequestBody ClaimRequest request,
                                        Authentication authentication,
                                        UriComponentsBuilder uriBuilder) {
        User user = userService.getByUsername(authentication.getName());
        Claim claim = claimService.submit(request, user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .location(uriBuilder.path("/api/me/claims/{id}").buildAndExpand(claim.getId()).toUri())
                .body(claim);
    }

    /** My submitted claims with their current status. */
    @GetMapping("/me/claims")
    public List<Claim> myClaims(Authentication authentication) {
        User user = userService.getByUsername(authentication.getName());
        return claimService.getMyClaims(user);
    }
}
