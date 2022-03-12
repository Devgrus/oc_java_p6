package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.MemberTransactionDto;
import com.paymybuddy.webapp.model.Member;
import com.paymybuddy.webapp.model.Role;
import com.paymybuddy.webapp.repository.MemberTransactionRepository;
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
public class MemberTransactionServiceTest {

    @Mock
    MemberTransactionRepository memberTransactionRepository;

    @Mock
    MemberService memberService;

    @InjectMocks
    MemberTransactionService memberTransactionService;

    static Member member1;
    static Member member2;

    @BeforeAll
    static public void initAll() {
        member1 = Member.builder()
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
        member2 = Member.builder()
                .username("cd@gmail.com")
                .password("1x#231")
                .nickname("ba")
                .role(Role.USER)
                .bankAccount("11-1111-2222")
                .amount(BigDecimal.valueOf(0))
                .connections(new HashSet<>())
                .banktransitions(new ArrayList<>())
                .usertransitions(new ArrayList<>())
                .build();
    }



    @BeforeEach
    public void init() {
        member1.setRole(Role.USER);
        member1.setBankAccount("11-1111-1111");
        member1.setAmount(BigDecimal.valueOf(0));
        member1.setConnections(new HashSet<>());
        member1.setBanktransitions(new ArrayList<>());
        member1.setUsertransitions(new ArrayList<>());

        member2.setRole(Role.USER);
        member1.setBankAccount("11-1111-2222");
        member2.setAmount(BigDecimal.valueOf(0));
        member2.setConnections(new HashSet<>());
        member2.setBanktransitions(new ArrayList<>());
        member2.setUsertransitions(new ArrayList<>());
    }

    @Test
    public void remitTest() {
        //given
        member1.setAmount(BigDecimal.valueOf(5000L));
        member2.setAmount(BigDecimal.valueOf(0L));

        Optional<Member> optionalMember1 = Optional.of(member1);
        Optional<Member> optionalMember2 = Optional.of(member2);

        MemberTransactionDto memberTransactionDto = MemberTransactionDto.builder()
                .receiver("cd@gmail.com")
                .amount(BigDecimal.valueOf(1000L))
                .description("for you")
                .build();

        //when
        when(memberService.findByUsername(member1.getUsername())).thenReturn(optionalMember1);
        when(memberService.findByUsername(member2.getUsername())).thenReturn(optionalMember2);

        //then
        memberTransactionService.remit("ab@gmail.com", memberTransactionDto);
        assertThat(member2.getAmount()).isEqualTo(BigDecimal.valueOf(1000L));
    }
}
