package com.SCAR.Authentication;

import com.SCAR.Account.AccountNotValidException;
import com.SCAR.Account.AccountService;
import com.SCAR.Account.SignUpForm;
import com.SCAR.Account.SignUpFormValidator;
import com.SCAR.Domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final SignUpFormValidator signUpFormValidator;

    @PostMapping("/auth/sign-up")
    public ResponseEntity<Map<String, String>> submitSignUp(@Valid @RequestBody SignUpForm signupForm, BindingResult bindingResult) {
        signUpFormValidator.validate(signupForm, bindingResult);
        if(bindingResult.hasErrors()) {
            List<String> errorList = authenticationService.getSignUpErrorList(bindingResult);
            throw new AccountNotValidException(errorList, "Custom Validator work");
        }
        return authenticationService.getSuccessSignUpResponse(signupForm);
    }

    @GetMapping("/auth/log-in")
    public String logIn(@RequestParam String email, @RequestParam String password) {
        return authenticationService.loginWithEmailAndPassword(email, password);
    }

}
