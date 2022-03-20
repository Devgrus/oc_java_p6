package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.dto.ConnectionDto;
import com.paymybuddy.webapp.model.CustomUserDetails;
import com.paymybuddy.webapp.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/contact")
public class ContactController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final MemberService memberService;

    public ContactController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public String contactPage(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
        logger.info("Request Received: GET /contact");
        model.addAttribute("connectionList", memberService.getAllConnections(customUserDetails.getUsername()));
        return "contact";
    }

    @PostMapping
    public String addConnection(@AuthenticationPrincipal CustomUserDetails customUserDetails, ConnectionDto connectionDto, RedirectAttributes rttr) {
        try {
            memberService.addConnection(customUserDetails.getUsername(), connectionDto);
        } catch(IllegalStateException e) {
            rttr.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/contact";
    }

    @DeleteMapping("/{username}")
    public String deleteConnection(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable String username, RedirectAttributes rttr) {
        memberService.deleteConnection(customUserDetails.getUsername(), username);
        return "redirect:/contact";
    }
}
