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

    @GetMapping("/account/{id}")
    public ResponseEntity<Account> retrieveOneUser(@PathVariable Long id) {
        return accountService.getUser(id);
    }

    @GetMapping("/account")
    public List<Account> retrieveAllUser() {
        return accountService.getAllUser();
    }

}
