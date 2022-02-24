package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.SignupDto;
import com.paymybuddy.webapp.model.Member;
import com.paymybuddy.webapp.model.Role;
import com.paymybuddy.webapp.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    MemberRepository memberRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    MemberService memberService;

    @Test
    public void saveTest() {
        //given
        SignupDto signupDto = SignupDto.builder()
                .username("abc@gmail.com")
                .password("12345")
                .nickname("thisuser")
                .build();

        Member member = Member.builder()
                .username(signupDto.getUsername())
                .password("1x#231")
                .nickname(signupDto.getNickname())
                .role(Role.GUEST)
                .bankAccount(null)
                .amount(BigDecimal.valueOf(0))
                .connections(null)
                .banktransitions(null)
                .usertransitions(null)
                .build();

        //when
        when(memberRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("1x#231");
        when(memberRepository.save(any())).thenReturn(member);

        //then
        assertThat(memberService.save(signupDto).getUsername()).isEqualTo("abc@gmail.com");
    }

    @Test
    public void saveTestUsernameAlreadyExist() {
        //given
        SignupDto signupDto = SignupDto.builder()
                .username("abc@gmail.com")
                .password("12345")
                .nickname("thisuser")
                .build();

        //when
        when(memberRepository.findByUsername(anyString())).thenReturn(Optional.of(new Member()));


        //then
        assertThatIllegalStateException().isThrownBy(()->memberService.save(signupDto));
    }

}
