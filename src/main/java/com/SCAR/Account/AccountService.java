package com.SCAR.Account;

import com.SCAR.Domain.Account;
import com.SCAR.Authentication.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Account> getAllUser() {
        return accountRepository.findAll();
    }

    public ResponseEntity<Account> getUser(Long id) {
        Optional<Account> findAccount = accountRepository.findById(id);
        if(findAccount.isEmpty()) throw new AccountNotFoundException(String.format("ID[%s]를 찾을 수 없습니다", id));

        return ResponseEntity.ok(findAccount.get());
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(email);
        if(account == null) {
            throw new UsernameNotFoundException(String.format("EMAIL : [%s]를 찾을 수 없습니다", email));
        }
        return account;
    }
}
