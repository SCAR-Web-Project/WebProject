package com.SCAR.Authentication;

import com.SCAR.Account.AccountRepository;
import com.SCAR.Account.AccountService;
import com.SCAR.Account.SignUpForm;
import com.SCAR.Domain.Account;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AccountService accountService;

    @BeforeEach
    void beforeEach() {

    }

    @AfterEach
    void afterEach() {
        accountRepository.deleteAll();
    }

    @DisplayName("signUp Test")
    @Test
    public void signUpTest() throws Exception {
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setEmail("chanho@email.com");
        signUpForm.setNickname("chanho");
        signUpForm.setPassword("12345678");

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/sign-up")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(signUpForm)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(authenticated());

        String authUser = accountService.loadUserByUsername("chanho@email.com").getUsername();
        System.out.println("AUTH USER: " +authUser);

        List<Account> all = accountRepository.findAll();
        assertEquals(all.get(0).getEmail(), "chanho@email.com");
        assertEquals(all.get(0).getNickname(), "chanho");
        assertTrue(passwordEncoder.matches("12345678", all.get(0).getPassword()));
    }
}