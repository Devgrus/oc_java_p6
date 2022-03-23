package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.dto.BankTransactionDto;
import com.paymybuddy.webapp.model.CustomUserDetails;
import com.paymybuddy.webapp.service.BankTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/bankTransaction")
public class BankTransactionController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final BankTransactionService bankTransactionService;

    public BankTransactionController(BankTransactionService bankTransactionService) {
        this.bankTransactionService = bankTransactionService;
    }

    /**
     * bank transaction page
     * @param customUserDetails user information
     * @param rttr send attribute to redirect page
     * @return if user has bank account, then return bankTransaction page. Else, bankAccount page.
     */
    @GetMapping
    public String bankTransactionPage(@AuthenticationPrincipal CustomUserDetails customUserDetails, RedirectAttributes rttr) {
        logger.info("Request Received: GET /bankTransaction");
        try {
            if(!bankTransactionService.isBankAccountExist(customUserDetails.getUsername())) {
                rttr.addFlashAttribute("errorMessage", "Need to add your bank account");
                return "redirect:/profile/bankAccount";
            }
        } catch (IllegalStateException e) {
            logger.error(e.getMessage());
        }

        return "bankTransaction";
    }

    /**
     * send money to bank
     * @param customUserDetails user information
     * @param bankTransactionDto bank transaction information
     * @param rttr send attribute to redirect page
     * @return if user has bank account, then return bankTransaction page. Else, bankAccount page.
     */
    @PostMapping("/send")
    public String send(@AuthenticationPrincipal CustomUserDetails customUserDetails, BankTransactionDto bankTransactionDto, RedirectAttributes rttr) {
        logger.info("Request Received: POST /bankTransaction/send");
        logger.info("Amount: " + bankTransactionDto.getAmount());
        try {
            if(!bankTransactionService.isBankAccountExist(customUserDetails.getUsername())) {
                rttr.addFlashAttribute("errorMessage", "Need to add your bank account");
                return "redirect:/profile/bankAccount";
            }

            bankTransactionService.sendToBank(customUserDetails.getUsername(), bankTransactionDto.getAmount());
        } catch(IllegalStateException e) {
            rttr.addFlashAttribute("errorMessage", e.getMessage());
            logger.error(e.getMessage());
        }
        return "redirect:/bankTransaction";
    }

    /**
     * receive money from bank
     * @param customUserDetails user information
     * @param bankTransactionDto bank transaction information
     * @param rttr send attribute to redirect page
     * @return if user has bank account, then return bankTransaction page. Else, bankAccount page.
     */
    @PostMapping("/receive")
    public String receive(@AuthenticationPrincipal CustomUserDetails customUserDetails, BankTransactionDto bankTransactionDto, RedirectAttributes rttr) {
        logger.info("Request Received: POST /bankTransaction/receive");
        logger.info("Amount: " + bankTransactionDto.getAmount());
        try {
            if(!bankTransactionService.isBankAccountExist(customUserDetails.getUsername())) {
                rttr.addFlashAttribute("errorMessage", "Need to add your bank account");
                return "redirect:/profile/bankAccount";
            }

            bankTransactionService.receiveFromBank(customUserDetails.getUsername(), bankTransactionDto.getAmount());
        } catch(IllegalStateException e) {
            rttr.addAttribute("errorMessage", e.getMessage());
            logger.error(e.getMessage());
        }
        return "redirect:/bankTransaction";
    }
}
