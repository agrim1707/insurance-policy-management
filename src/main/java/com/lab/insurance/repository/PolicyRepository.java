package com.lab.insurance.repository;

import com.lab.insurance.model.Policy;
import com.lab.insurance.model.PolicyCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PolicyRepository extends JpaRepository<Policy, Long> {

    List<Policy> findByActiveTrue();

    List<Policy> findByCategoryAndActiveTrue(PolicyCategory category);
}
