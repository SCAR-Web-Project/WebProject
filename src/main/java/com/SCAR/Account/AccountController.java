package com.SCAR.Account;

import com.SCAR.Domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class AccountController {

    @Autowired
    AccountService accountService;

    @GetMapping("/account")
    public List<Account> retrieveAllUser() {
        return accountService.getAllUser();
    }

    @PostMapping("/account/sign-up")
    public Long submitSignUp(@Valid @RequestBody SignupForm signupForm, Errors errors) {

//        accountService.login(newAccount);
        // TODO  계정 생성했을 때 자동으로 로그인 구현
        return accountService.processNewAccount(signupForm);
    }

}
