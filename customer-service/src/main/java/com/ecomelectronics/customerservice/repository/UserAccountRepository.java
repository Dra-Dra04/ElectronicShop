package com.ecomelectronics.customerservice.repository;

import com.ecomelectronics.customerservice.model.UserAccount;
import com.ecomelectronics.customerservice.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    Optional<UserAccount> findByEmail(String email);

    List<UserAccount> findByRole(UserRole role);

    List<UserAccount> findByActive(boolean active);
}
