package com.paymybuddy.webapp.Controller;

import com.paymybuddy.webapp.config.WithMockCustomUser;
import com.paymybuddy.webapp.controller.BankTransactionController;
import com.paymybuddy.webapp.service.BankTransactionService;
import com.paymybuddy.webapp.service.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
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


}
