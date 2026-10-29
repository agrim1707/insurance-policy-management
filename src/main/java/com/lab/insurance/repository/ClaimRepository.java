package com.lab.insurance.repository;

import com.lab.insurance.model.Claim;
import com.lab.insurance.model.ClaimStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClaimRepository extends JpaRepository<Claim, Long> {

    List<Claim> findByUserOrderBySubmittedAtDesc(com.lab.insurance.model.User user);

    List<Claim> findByStatusOrderBySubmittedAtDesc(ClaimStatus status);

    List<Claim> findAllByOrderBySubmittedAtDesc();
}
