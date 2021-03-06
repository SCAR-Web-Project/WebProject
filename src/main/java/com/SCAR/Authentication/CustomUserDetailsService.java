package com.SCAR.Authentication;

import com.SCAR.Account.AccountRepository;
import com.SCAR.Domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    
    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(email);
        if(account == null) {
            throw new UsernameNotFoundException(String.format("EMAIL : [%s]를 찾을 수 없습니다", email));
        }
        System.out.println("user authority: "+account.getAuthorities());
        return new User(account.getEmail(), account.getPassword(), account.getAuthorities());
    }
}
