package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.CustomUserDetails;
import com.paymybuddy.webapp.model.Member;
import com.paymybuddy.webapp.model.Role;
import com.paymybuddy.webapp.repository.BankTransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BankTransactionServiceTest {

    @Mock
    BankTransactionRepository bankTransactionRepository;

    @Mock
    MemberService memberService;

    @InjectMocks
    BankTransactionService bankTransactionService;

    @Test
    public void sendToBankTest() {
        //given
        Member member = Member.builder()
                .username("ab@gmail.com")
                .password("1x#231")
                .nickname("ab")
                .role(Role.GUEST)
                .bankAccount("12345")
                .amount(BigDecimal.valueOf(200))
                .connections(new HashSet<>())
                .banktransitions(new ArrayList<>())
                .usertransitions(new ArrayList<>())
                .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(member);

        //when
        when(memberService.findByUsername(anyString())).thenReturn(Optional.of(member));
        when(bankTransactionRepository.save(any())).thenReturn(any());

        //then
        assertThat(bankTransactionService.sendToBank(customUserDetails, BigDecimal.TEN)).isTrue();
    }

    @Test
    public void sendToBankTestMemberHasLessThanTwoPointOneEuro() {
        //given
        Member member = Member.builder()
                .username("ab@gmail.com")
                .password("1x#231")
                .nickname("ab")
                .role(Role.GUEST)
                .bankAccount("12345")
                .amount(new BigDecimal("2.0"))
                .connections(new HashSet<>())
                .banktransitions(new ArrayList<>())
                .usertransitions(new ArrayList<>())
                .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(member);

        //when
        when(memberService.findByUsername(anyString())).thenReturn(Optional.of(member));

        //then
        assertThatIllegalStateException().isThrownBy(()->bankTransactionService.sendToBank(customUserDetails, BigDecimal.TEN));
    }

    @Test
    public void sendToBankTestMemberSendLessThanTwoPointOneEuro() {
        //given
        Member member = Member.builder()
                .username("ab@gmail.com")
                .password("1x#231")
                .nickname("ab")
                .role(Role.GUEST)
                .bankAccount("12345")
                .amount(new BigDecimal("2.5"))
                .connections(new HashSet<>())
                .banktransitions(new ArrayList<>())
                .usertransitions(new ArrayList<>())
                .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(member);

        //when
        when(memberService.findByUsername(anyString())).thenReturn(Optional.of(member));

        //then
        assertThatIllegalStateException().isThrownBy(()->bankTransactionService.sendToBank(customUserDetails, BigDecimal.ONE));
    }

    @Test
    public void sendToBankTestMemberHasLessThanSendAmount() {
        //given
        Member member = Member.builder()
                .username("ab@gmail.com")
                .password("1x#231")
                .nickname("ab")
                .role(Role.GUEST)
                .bankAccount("12345")
                .amount(new BigDecimal("8"))
                .connections(new HashSet<>())
                .banktransitions(new ArrayList<>())
                .usertransitions(new ArrayList<>())
                .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(member);

        //when
        when(memberService.findByUsername(anyString())).thenReturn(Optional.of(member));

        //then
        assertThatIllegalStateException().isThrownBy(()->bankTransactionService.sendToBank(customUserDetails, BigDecimal.TEN));
    }

    @Test
    public void receiveFromBankTest() {
        //given
        Member member = Member.builder()
                .username("ab@gmail.com")
                .password("1x#231")
                .nickname("ab")
                .role(Role.GUEST)
                .bankAccount("12345")
                .amount(new BigDecimal("200"))
                .connections(new HashSet<>())
                .banktransitions(new ArrayList<>())
                .usertransitions(new ArrayList<>())
                .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(member);

        //when
        when(memberService.findByUsername(anyString())).thenReturn(Optional.of(member));
        when(bankTransactionRepository.save(any())).thenReturn(any());

        //then
        assertThat(bankTransactionService.receiveFromBank(customUserDetails, BigDecimal.TEN)).isTrue();
    }

    @Test
    public void receiveFromBankTestMemberReceiveLessThanTwoPointOneEuro() {
        //given
        Member member = Member.builder()
                .username("ab@gmail.com")
                .password("1x#231")
                .nickname("ab")
                .role(Role.GUEST)
                .bankAccount("12345")
                .amount(new BigDecimal("200"))
                .connections(new HashSet<>())
                .banktransitions(new ArrayList<>())
                .usertransitions(new ArrayList<>())
                .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(member);

        //when
        when(memberService.findByUsername(anyString())).thenReturn(Optional.of(member));

        //then
        assertThatIllegalStateException().isThrownBy(()->bankTransactionService.sendToBank(customUserDetails, BigDecimal.ONE));
    }
}
