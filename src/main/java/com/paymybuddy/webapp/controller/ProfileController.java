package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.dto.BankAccountUpdateDto;
import com.paymybuddy.webapp.model.CustomUserDetails;
import com.paymybuddy.webapp.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final MemberService memberService;

    @Autowired
    public ProfileController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public String profilePage(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        logger.info("Request Received: GET /profile");
        customUserDetails.getAuthorities().stream().forEach(i->System.out.println(i.getAuthority()));
        return "profile";
    }

    @GetMapping("/bankAccount")
    public String bankAccountPage() {
        logger.info("Request Received: GET /profile/bankAccount");
        return "bankAccount";
    }

    @PostMapping("/bankAccount")
    public String setBankAccount(@AuthenticationPrincipal CustomUserDetails customUserDetails, BankAccountUpdateDto bankAccountUpdateDto, RedirectAttributes rttr) {
        logger.info("Request Received: POST /profile/bankAccount");
        memberService.setBankAccount(customUserDetails.getUsername(), bankAccountUpdateDto);
        return "redirect:/profile";
    }
}
