package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.dto.MemberTransactionDto;
import com.paymybuddy.webapp.dto.MemberTransactionListDto;
import com.paymybuddy.webapp.model.CustomUserDetails;
import com.paymybuddy.webapp.model.Member;
import com.paymybuddy.webapp.service.MemberService;
import com.paymybuddy.webapp.service.MemberTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Set;

@Controller
@RequestMapping("/memberTransaction")
public class MemberTransactionController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final MemberTransactionService memberTransactionService;
    private final MemberService memberService;

    public MemberTransactionController(MemberTransactionService memberTransactionService, MemberService memberService) {
        this.memberTransactionService = memberTransactionService;
        this.memberService = memberService;
    }

    @GetMapping
    public String memberTransactionPage(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PageableDefault(size = 5) Pageable pageable, Model model) {
        logger.info("Request Received: GET /memberTransaction");
        try {
            Set<Member> connections = memberService.getAllConnections(customUserDetails.getUsername());
            Page<MemberTransactionListDto> memberTransactionListDtoList = memberTransactionService.transactionList(customUserDetails.getUsername(), pageable);
            model.addAttribute("connections", connections);
            model.addAttribute("transactionList", memberTransactionListDtoList);
        } catch(IllegalStateException e) {
            logger.error(e.getMessage());
        }
        return "memberTransaction";
    }

    @PostMapping
    public String sendMoney(@AuthenticationPrincipal CustomUserDetails customUserDetails, MemberTransactionDto memberTransactionDto, RedirectAttributes rttr) {
        logger.info("Request Received: POST /memberTransaction");
        try {
            memberTransactionService.remit(customUserDetails.getUsername(), memberTransactionDto);
        } catch(IllegalStateException e) {
            rttr.addFlashAttribute("errorMessage", e.getMessage());
            logger.error(e.getMessage());
        }
        return "redirect:/memberTransaction";
    }
}
