package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.dto.SignupDto;
import com.paymybuddy.webapp.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/signup")
public class SignupController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final MemberService memberService;

    public SignupController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public String signupForm() {
        logger.info("Request received: GET /signup");
        return "signup";
    }

    @PostMapping
    public String signup(SignupDto signupDto, Model model) {
        logger.info("Request received: POST /signup");

        try {
            memberService.save(signupDto);
        } catch(IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/signup";
        }
        return "redirect:/login";
    }
}
