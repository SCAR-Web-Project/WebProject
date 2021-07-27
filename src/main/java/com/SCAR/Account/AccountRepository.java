package com.SCAR.Account;

import com.SCAR.Domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    Account findByEmail(String email);
}
