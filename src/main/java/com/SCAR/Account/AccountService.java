package com.SCAR.Account;

import com.SCAR.Domain.Account;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Account processNewAccount(SignUpForm signupForm) {
        // TODO send email and confirm
        return saveNewAccount(signupForm);
    }

    private Account saveNewAccount(SignUpForm signupForm) {
        Account newAccount = Account.builder()
                .email(signupForm.getEmail())
                .nickname(signupForm.getNickname())
                .SKKEmail(signupForm.getSKKEmail())
                .password(passwordEncoder.encode(signupForm.getPassword()))
                .Role(RoleType.ROLE_USER)
                .build();

        return accountRepository.save(newAccount);
    }

    public List<Account> getAllUser() {
        return accountRepository.findAll();
    }

    public ResponseEntity<Account> getUser(Long id) {
        Optional<Account> findAccount = accountRepository.findById(id);
        if(findAccount.isEmpty()) throw new AccountNotFoundException(String.format("ID[%s]를 찾을 수 없습니다", id));

        return ResponseEntity.ok(findAccount.get());
    }
}
