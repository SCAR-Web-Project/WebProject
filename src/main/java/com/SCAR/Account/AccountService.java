package com.SCAR.Account;

import com.SCAR.Domain.Account;
import com.SCAR.Authentication.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public Account processNewAccount(SignUpForm signupForm) {
        // TODO send email and confirm
        return saveNewAccount(signupForm);
    }

    private Account saveNewAccount(SignUpForm signupForm) {
        Account newAccount = Account.builder()
                .email(signupForm.getEmail())
                .nickname(signupForm.getNickname())
                .roles(Collections.singletonList("ROLE_USER"))
                .password(passwordEncoder.encode(signupForm.getPassword()))
                .build();

        return accountRepository.save(newAccount);
    }

    private void getAuthentication(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);
        authenticationManager.authenticate(authenticationToken);
    }

    private String getJwtToken(Account account) {
        return jwtTokenProvider.createToken(account.getUsername(), account.getAuthorities()
                .stream().map(Object::toString).collect(Collectors.toList()));
    }

    public List<Account> getAllUser() {
        return accountRepository.findAll();
    }

    public ResponseEntity<Account> getUser(Long id) {
        Optional<Account> findAccount = accountRepository.findById(id);
        if(findAccount.isEmpty()) throw new AccountNotFoundException(String.format("ID[%s]를 찾을 수 없습니다", id));

        return ResponseEntity.ok(findAccount.get());
    }

    public List<String> getSignUpErrorList(BindingResult bindingResult) {
        return bindingResult.getGlobalErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
    }

    public ResponseEntity<Map<String, String>> getSuccessSignUpResponse(SignUpForm signupForm) {
        Account newAccount = processNewAccount(signupForm);
        String jwtToken = loginWithEmailAndPassword(signupForm.getEmail(), signupForm.getPassword());

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("UserId", newAccount.getId().toString());
        responseBody.put("Token", jwtToken);
        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }

    public String loginWithEmailAndPassword(String email, String password) {
        Account account = accountRepository.findByEmail(email);
        if(account==null) {
            throw new AccountNotFoundException(String.format("Email[%s]를 찾을 수 없습니다", email));
        }
        if(!passwordEncoder.matches(password, account.getPassword())) {
            throw new IllegalArgumentException("패스워드가 일치하지 않습니다.");
        }
        getAuthentication(email, password);
        return getJwtToken(account);
    }
}
