package com.paymybuddy.webapp.config;

import com.paymybuddy.webapp.model.CustomUserDetails;
import com.paymybuddy.webapp.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.util.Assert;

import java.math.BigDecimal;


public class WithUserDetailsSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    public SecurityContext createSecurityContext(WithMockCustomUser withMockCustomUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Member member = Member.builder()
                .username(withMockCustomUser.username())
                .password(withMockCustomUser.password())
                .nickname(withMockCustomUser.nickname())
                .role(withMockCustomUser.role())
                .bankAccount(withMockCustomUser.bankAccount())
                .amount(new BigDecimal(withMockCustomUser.amount())).build();
        CustomUserDetails principal = new CustomUserDetails(member);
        Authentication auth = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}
