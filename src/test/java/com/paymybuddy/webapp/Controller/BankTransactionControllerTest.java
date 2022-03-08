package com.paymybuddy.webapp.Controller;

import com.paymybuddy.webapp.config.WithMockCustomUser;
import com.paymybuddy.webapp.controller.BankTransactionController;
import com.paymybuddy.webapp.service.BankTransactionService;
import com.paymybuddy.webapp.service.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(BankTransactionController.class)
public class BankTransactionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BankTransactionService bankTransactionService;

    @MockBean
    CustomUserDetailsService customUserDetailsService;

    @Test
    @WithMockCustomUser
    public void sendTest() throws Exception {
        //given


        //when
        when(bankTransactionService.sendToBank(any(), any())).thenReturn(true);

        //then
        mockMvc.perform(post("/bankTransaction/send")
                        .param("amount", "1000"))
                .andExpect(redirectedUrl("/bankTransaction"));
    }

    @Test
    @WithMockCustomUser
    public void sendTestNotEnoughMoney() throws Exception {
        //given


        //when
        when(bankTransactionService.sendToBank(any(), any())).thenThrow(new IllegalStateException("Not enough money"));

        //then
        mockMvc.perform(post("/bankTransaction/send")
                        .param("amount", "1000000"))
                .andExpect(flash().attribute("errorMessage", "Not enough money"));
    }

    @Test
    @WithMockCustomUser
    public void sendTestLessThanOneEuro() throws Exception {
        //given


        //when
        when(bankTransactionService.sendToBank(any(), any())).thenThrow(new IllegalStateException("Minimum 1€"));

        //then
        mockMvc.perform(post("/bankTransaction/send")
                        .param("amount", "1000000"))
                .andExpect(flash().attribute("errorMessage", "Minimum 1€"));
    }

    @Test
    @WithMockCustomUser
    public void receiveTest() throws Exception {
        //given


        //when
        when(bankTransactionService.sendToBank(any(), any())).thenReturn(true);

        //then
        mockMvc.perform(post("/bankTransaction/receive")
                        .param("amount", "1000"))
                .andExpect(redirectedUrl("/bankTransaction"));
    }

    @Test
    @WithMockCustomUser
    public void receiveTestLessThanOneEuro() throws Exception {
        //given


        //when
        when(bankTransactionService.sendToBank(any(), any())).thenThrow(new IllegalStateException("Minimum 1€"));

        //then
        mockMvc.perform(post("/bankTransaction/send")
                        .param("amount", "1000000"))
                .andExpect(flash().attribute("errorMessage", "Minimum 1€"));
    }


}
