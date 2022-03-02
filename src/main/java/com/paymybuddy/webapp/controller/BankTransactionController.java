package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.dto.BankTransactionDto;
import com.paymybuddy.webapp.model.CustomUserDetails;
import com.paymybuddy.webapp.service.BankTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/bankTransaction")
public class BankTransactionController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final BankTransactionService bankTransactionService;

    public BankTransactionController(BankTransactionService bankTransactionService) {
        this.bankTransactionService = bankTransactionService;
    }

    @PostMapping("/send")
    public String send(@AuthenticationPrincipal CustomUserDetails customUserDetails, BankTransactionDto bankTransactionDto, Model model) {
        try {
            bankTransactionService.sendToBank(customUserDetails, bankTransactionDto.getAmount());
        } catch(IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());

        }
        return "redirect:/bankTransaction";
    }

    @PostMapping("/receive")
    public String receive() {
        return "";
    }
}
