package com.SCAR.Account;

import com.SCAR.Domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class AccountController {

    @Autowired
    AccountService accountService;

    @GetMapping("/account/{id}")
    public ResponseEntity<Account> retrieveOneUser(@PathVariable Long id) {
        return accountService.getUser(id);
    }

    @GetMapping("/account")
    public List<Account> retrieveAllUser() {
        return accountService.getAllUser();
    }

    @PostMapping("/account")
    public ResponseEntity<Account> submitSignUp(@Valid @RequestBody SignupForm signupForm) {
//        accountService.login(newAccount);
//        if(errors.hasErrors()) throw new AccountArgumentNotValidException()

        Account newAccount = accountService.processNewAccount(signupForm);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newAccount.getId())
                .toUri();
        // TODO  계정 생성했을 때 자동으로 로그인 구현
        return ResponseEntity.created(location).build();
    }
}
