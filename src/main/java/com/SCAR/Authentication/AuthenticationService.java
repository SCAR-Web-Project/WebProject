package com.SCAR.Authentication;

import com.SCAR.Account.AccountNotFoundException;
import com.SCAR.Account.AccountRepository;
import com.SCAR.Account.SignUpForm;
import com.SCAR.Domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    private final AuthenticationManager authenticationManager;

    public String loginWithEmailAndPassword(String email, String password,
                                            HttpServletRequest httpServletRequest) {
        Account account = accountRepository.findByEmail(email);
        if(account==null) {
            throw new AccountNotFoundException(String.format("Email[%s]를 찾을 수 없습니다", email));
        }
        if(!passwordEncoder.matches(password, account.getPassword())) {
            throw new IllegalArgumentException("패스워드가 일치하지 않습니다.");
        }

        return getAuthentication(account, httpServletRequest);
    }

    private String getJwtToken(Account account) {
        return jwtTokenProvider.createToken(account.getUsername(), account.getAuthorities()
                .stream().map(Object::toString).collect(Collectors.toList()));
    }

    public ResponseEntity<Map<String, String>> getSuccessSignUpResponse(SignUpForm signupForm, HttpServletRequest httpServletRequest) {
        String jwtToken = loginWithEmailAndPassword(signupForm.getEmail(), signupForm.getPassword(), httpServletRequest);
        Map<String, String> responseBody = new HashMap<>();

        Account account = accountRepository.findByEmail(signupForm.getEmail());
        responseBody.put("userId", account.getId().toString());
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
                .roles(List.of("ROLE_USER"))
                .password(passwordEncoder.encode(signupForm.getPassword()))
                .build();

        return accountRepository.save(newAccount);
    }

    public String getAuthentication(Account account, HttpServletRequest httpServletRequest) {
        String token = getJwtToken(account);
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Authentication a = SecurityContextHolder.getContext().getAuthentication();

        if(SecurityContextHolder.getContext().getAuthentication() == null) {
            System.out.println("Service Authentication Null");
        } else {
            System.out.println("Service Authentication Exists");
        }

        return token;
    }
}
