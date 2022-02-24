package com.paymybuddy.webapp.Controller;

import com.paymybuddy.webapp.controller.SignupController;
import com.paymybuddy.webapp.model.Member;
import com.paymybuddy.webapp.service.CustomUserDetailsService;
import com.paymybuddy.webapp.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(SignupController.class)
public class SignupControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CustomUserDetailsService customUserDetailsService;

    @MockBean
    MemberService memberService;

    @Test
    @WithAnonymousUser
    public void getSignupPage() throws Exception {
        //given

        //when
        when(memberService.save(any())).thenReturn(new Member());

        //then
        mockMvc.perform(get("/signup"))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void signupRedirectTest() throws Exception {
        //given

        //when
        when(memberService.save(any())).thenReturn(new Member());

        //then
        mockMvc.perform(post("/signup")
                .param("username", "abc@gmail.com")
                .param("password", "xxx")
                .param("nickname", "thisname"))
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    public void signupRedirectTestWithIllegalStateException() throws Exception {
        //given

        //when
        when(memberService.save(any())).thenThrow(IllegalStateException.class);

        //then
        mockMvc.perform(post("/signup")
                        .param("username", "abc@gmail.com")
                        .param("password", "xxx")
                        .param("nickname", "thisname"))
                .andExpect(redirectedUrl("/signup"));
    }
}
