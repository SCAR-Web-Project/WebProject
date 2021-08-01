package com.SCAR.Authentication;

import com.SCAR.Account.AccountNotFoundException;
import com.SCAR.Account.AccountRepository;
import com.SCAR.Account.SignUpForm;
import com.SCAR.Domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class AuthenticationService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public String loginWithEmailAndPassword(String email, String password) {
        Account account = accountRepository.findByEmail(email);
        if(account==null) {
            throw new AccountNotFoundException(String.format("Email[%s]를 찾을 수 없습니다", email));
        }
        if(!passwordEncoder.matches(password, account.getPassword())) {
            throw new IllegalArgumentException("패스워드가 일치하지 않습니다.");
        }
        return getJwtToken(account);
    }

    private String getJwtToken(Account account) {
        return jwtTokenProvider.createToken(account.getUsername(), account.getAuthorities()
                .stream().map(Object::toString).collect(Collectors.toList()));
    }

    public ResponseEntity<Map<String, String>> getSuccessSignUpResponse(SignUpForm signupForm) {
        Account newAccount = processNewAccount(signupForm);
        String jwtToken = loginWithEmailAndPassword(signupForm.getEmail(), signupForm.getPassword());

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("UserId", newAccount.getId().toString());
        responseBody.put("Token", jwtToken);
        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }

    public List<String> getSignUpErrorList(BindingResult bindingResult) {
        return bindingResult.getGlobalErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
    }

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

    //    private void getAuthentication(Account account) {
//        UsernamePasswordAuthenticationToken authenticationToken =
//                new UsernamePasswordAuthenticationToken(
//                        account.getEmail(),
//                        account.getPassword(),
//                        List.of(new SimpleGrantedAuthority("ROLE_USER")));
//        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//    }
}
