package com.SCAR.Account;

import com.SCAR.Domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final SignUpFormValidator signUpFormValidator;

//    @InitBinder("signupForm")
//    public void initBinder(WebDataBinder webDataBinder) {
//        webDataBinder.addValidators(signUpFormValidator);
//    }

    @GetMapping("/account/{id}")
    public ResponseEntity<Account> retrieveOneUser(@PathVariable Long id) {
        return accountService.getUser(id);
    }

    @GetMapping("/account")
    public List<Account> retrieveAllUser() {
        return accountService.getAllUser();
    }

    @PostMapping("/account")
    public ResponseEntity<Account> submitSignUp(@Valid @RequestBody SignUpForm signupForm, BindingResult bindingResult) {
//        accountService.login(newAccount);
//        if(errors.hasErrors()) throw new AccountArgumentNotValidException()
        signUpFormValidator.validate(signupForm, bindingResult);
        if(bindingResult.hasErrors()) {
            List<String> errorList =
                    bindingResult.getGlobalErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            throw new AccountNotValidException(errorList, "Custom Validator work");
        }

        Account newAccount = accountService.processNewAccount(signupForm);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newAccount.getId())
                .toUri();
        // TODO  계정 생성했을 때 자동으로 로그인 구현
        return ResponseEntity.created(location).build();
    }
}
