package com.lab.insurance.repository;

import com.lab.insurance.model.User;
import com.lab.insurance.model.UserPolicy;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPolicyRepository extends JpaRepository<UserPolicy, Long> {

    List<UserPolicy> findByUser(User user);
}
