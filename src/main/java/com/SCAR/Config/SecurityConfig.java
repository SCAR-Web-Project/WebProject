package com.SCAR.Config;

import com.SCAR.Authentication.CustomUserDetailsService;
import com.SCAR.Authentication.JwtAuthenticationFilter;
import com.SCAR.Authentication.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception{
        authenticationManagerBuilder.userDetailsService(customUserDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .httpBasic().disable() // rest api -> 기본 설정 disable
                .csrf().disable() // csrf -> 비활성화, security 설정 시 기본값으로 활성화 되어있음
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 활용 안함
                .and()
                    .authorizeRequests()
                        .antMatchers("/auth/log-in", "/auth/sign-up", "/post/list",
                                "/v2/api-docs", "/configuration/ui", "/swagger-resources/**",
                                "/configuration/security", "/swagger-ui.html", "/webjars/**").permitAll() // 가입, 로그인에 대한 권한 해제
                        .anyRequest().hasRole("USER") // 나머지 요청에 대해 USER ROLE 을 가져야만 접근 가능
                .and()
                    .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
    }
}
