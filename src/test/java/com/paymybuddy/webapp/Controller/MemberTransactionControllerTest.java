package com.paymybuddy.webapp.Controller;

import com.paymybuddy.webapp.config.WithMockCustomUser;
import com.paymybuddy.webapp.controller.MemberTransactionController;
import com.paymybuddy.webapp.service.CustomUserDetailsService;
import com.paymybuddy.webapp.service.MemberTransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(MemberTransactionController.class)
public class MemberTransactionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MemberTransactionService memberTransactionService;

    @MockBean
    CustomUserDetailsService customUserDetailsService;

    @Test
    @WithMockCustomUser
    public void remitTest() throws Exception {
        //given

        //when

        //then
        mockMvc.perform(post("/memberTransaction")
                .param("username", "cd@test.com")
                .param("amount", "1000")
                .param("description", "money"))
                .andExpect(redirectedUrl("/memberTransaction"));
    }

    @Test
    @WithMockCustomUser
    public void remitTestNotEnoughMoney() throws Exception {
        //given

        //when
        doThrow(new IllegalStateException("Not enough money")).when(memberTransactionService).remit(any(), any());

        //then
        mockMvc.perform(post("/memberTransaction")
                        .param("username", "cd@test.com")
                        .param("amount", "1000")
                        .param("description", "money"))
                .andExpect(flash().attribute("errorMessage", "Not enough money"));
    }
}
