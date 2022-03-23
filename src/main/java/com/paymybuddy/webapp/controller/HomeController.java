package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.model.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    /**
     * main page
     * @param customUserDetails user information
     * @param model model
     * @return main page
     */
    @GetMapping("/")
    public String home(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
        if(customUserDetails != null) model.addAttribute("nickname", customUserDetails.getNickname());
        return "home";
    }
}
