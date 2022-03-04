package com.paymybuddy.webapp.config;

import com.paymybuddy.webapp.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder encryptPassword() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(encryptPassword());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/login", "/signup", "/h2-console/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and().csrf()
                .ignoringAntMatchers("/h2-console/**")
                .and()
                .headers().addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
                .and().formLogin()
                .loginPage("/login")
                .and().rememberMe()
                .key("pmb")
                .rememberMeParameter("remember-me")
                .tokenValiditySeconds(86400 * 30)
                .userDetailsService(customUserDetailsService)
                .and().logout()
                .logoutSuccessUrl("/")
//                .and().oauth2Login()
//                .loginPage("/login")
                .and().csrf().disable();

        // Accept only one session per member
        http
                .sessionManagement()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false);
    }


}
