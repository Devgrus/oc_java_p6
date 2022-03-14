package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.CustomUserDetails;
import com.paymybuddy.webapp.model.Member;
import com.paymybuddy.webapp.model.Role;
import com.paymybuddy.webapp.repository.BankTransactionRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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

    static Member member;

    @BeforeAll
    static public void initAll() {
        member = Member.builder()
                .id(1L)
                .username("ab@gmail.com")
                .password("1x#231")
                .nickname("thisuser")
                .role(Role.USER)
                .bankAccount("11-1111-1111")
                .amount(BigDecimal.valueOf(0))
                .connections(new HashSet<>())
                .banktransitions(new ArrayList<>())
                .usertransitions(new ArrayList<>())
                .build();
    }

    @BeforeEach
    public void init() {
        member.setRole(Role.USER);
        member.setBankAccount("11-1111-1111");
        member.setAmount(BigDecimal.valueOf(0));
        member.setConnections(new HashSet<>());
        member.setBanktransitions(new ArrayList<>());
        member.setUsertransitions(new ArrayList<>());
    }

    @Test
    public void sendToBankTest() {
        //given
        member.setAmount(BigDecimal.valueOf(1000L));

        CustomUserDetails customUserDetails = new CustomUserDetails(member);

        //when
        when(memberService.findByUsername(anyString())).thenReturn(Optional.of(member));
        when(bankTransactionRepository.save(any())).thenReturn(any());

        //then
        bankTransactionService.sendToBank(member.getUsername(), BigDecimal.TEN);
        assertThat(member.getAmount()).isEqualTo(new BigDecimal("989.95"));
    }

    @Test
    public void sendToBankTestMemberHasLessThanTwoPointOneEuro() {
        //given
        Member member = Member.builder()
                .username("ab@gmail.com")
                .password("1x#231")
                .nickname("ab")
                .role(Role.USER)
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
        assertThatIllegalStateException().isThrownBy(()->bankTransactionService.sendToBank(customUserDetails.getUsername(), BigDecimal.TEN));
    }

    @Test
    public void sendToBankTestMemberSendLessThanTwoPointOneEuro() {
        //given
        Member member = Member.builder()
                .username("ab@gmail.com")
                .password("1x#231")
                .nickname("ab")
                .role(Role.USER)
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
        assertThatIllegalStateException().isThrownBy(()->bankTransactionService.sendToBank(customUserDetails.getUsername(), BigDecimal.ONE));
    }

    @Test
    public void sendToBankTestMemberHasLessThanSendAmount() {
        //given
        Member member = Member.builder()
                .username("ab@gmail.com")
                .password("1x#231")
                .nickname("ab")
                .role(Role.USER)
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
        assertThatIllegalStateException().isThrownBy(()->bankTransactionService.sendToBank(customUserDetails.getUsername(), BigDecimal.TEN));
    }

    @Test
    public void receiveFromBankTest() {
        //given
        Member member = Member.builder()
                .username("ab@gmail.com")
                .password("1x#231")
                .nickname("ab")
                .role(Role.USER)
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
//        assertThat(bankTransactionService.receiveFromBank(customUserDetails.getUsername(), BigDecimal.TEN)).isTrue();
    }

    @Test
    public void receiveFromBankTestMemberReceiveLessThanTwoPointOneEuro() {
        //given
        Member member = Member.builder()
                .username("ab@gmail.com")
                .password("1x#231")
                .nickname("ab")
                .role(Role.USER)
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
        assertThatIllegalStateException().isThrownBy(()->bankTransactionService.sendToBank(customUserDetails.getUsername(), BigDecimal.ONE));
    }

    @Test
    public void isBankAccountExistTest() {
        //given
        member.setBankAccount(null);

        //when
        when(memberService.findByUsername(member.getUsername())).thenReturn(Optional.of(member));

        //then
        assertThat(bankTransactionService.isBankAccountExist(member.getUsername())).isFalse();

    }
}
