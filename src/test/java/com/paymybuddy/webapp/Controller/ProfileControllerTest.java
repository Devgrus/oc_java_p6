package com.paymybuddy.webapp.Controller;

import com.paymybuddy.webapp.config.WithMockCustomUser;
import com.paymybuddy.webapp.controller.ProfileController;
import com.paymybuddy.webapp.service.CustomUserDetailsService;
import com.paymybuddy.webapp.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(ProfileController.class)
public class ProfileControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MemberService memberService;

    @MockBean
    CustomUserDetailsService customUserDetailsService;


}
