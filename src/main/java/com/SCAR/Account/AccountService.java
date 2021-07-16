package com.SCAR.Account;

import com.SCAR.Domain.Account;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public Long processNewAccount(SignupForm signupForm) {
        // TODO send email and confirm
        return saveNewAccount(signupForm).getId();
    }

    private Account saveNewAccount(SignupForm signupForm) {
        Account newAccount = Account.builder()
                .email(signupForm.getEmail())
                .nickname(signupForm.getNickname())
                .SKKEmail(signupForm.getSKKEmail())
                .password(signupForm.getPassword())
                .Role(RoleType.ROLE_USER)
                // TODO password encoding
                .build();

        return accountRepository.save(newAccount);
    }

    public List<Account> getAllUser() {
        return accountRepository.findAll();
    }
}
