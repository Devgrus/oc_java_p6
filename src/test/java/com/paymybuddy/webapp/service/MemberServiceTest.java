package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.BankAccountUpdateDto;
import com.paymybuddy.webapp.dto.ConnectionDto;
import com.paymybuddy.webapp.dto.SignupDto;
import com.paymybuddy.webapp.model.CustomUserDetails;
import com.paymybuddy.webapp.model.Member;
import com.paymybuddy.webapp.model.Role;
import com.paymybuddy.webapp.repository.MemberRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    MemberRepository memberRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    MemberService memberService;

    static Member member1;
    static Member member2;

    @BeforeAll
    static public void initAll() {
        member1 = Member.builder()
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
        member2 = Member.builder()
                .id(2L)
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
    public void saveTest() {
        //given
        SignupDto signupDto = SignupDto.builder()
                .username("ab@gmail.com")
                .password("12345")
                .nickname("thisuser")
                .build();

        //when
        when(memberRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("1x#231");
        when(memberRepository.save(any())).thenReturn(member1);

        //then
        assertThat(memberService.save(signupDto).getUsername()).isEqualTo(member1.getUsername());
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

    @Test
    public void setBankAccountTest() {
        //given

        CustomUserDetails customUserDetails = new CustomUserDetails(member1);

        BankAccountUpdateDto bankAccountUpdateDto = BankAccountUpdateDto.builder()
                .bankAccount("111-111-1111")
                .build();

        Optional<Member> optionalMember1 = Optional.of(member1);

        //when
        when(memberService.findByUsername(anyString())).thenReturn(optionalMember1);

        //then
        memberService.setBankAccount(member1.getUsername(), bankAccountUpdateDto);
        assertThat(optionalMember1.get().getBankAccount()).isEqualTo("111-111-1111");
    }

    @Test
    public void addBankAccountTestMemberIsEmpty() {
        //given

        CustomUserDetails customUserDetails = new CustomUserDetails(member1);

        BankAccountUpdateDto bankAccountUpdateDto = BankAccountUpdateDto.builder()
                .bankAccount("111-111-1111")
                .build();

        //when
        when(memberRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        //then
        assertThatIllegalStateException().isThrownBy(()->memberService.setBankAccount(member1.getUsername(),bankAccountUpdateDto));
    }

    @Test
    public void addConnectionTest() {
        //given

        CustomUserDetails customUserDetails = new CustomUserDetails(member1);

        ConnectionDto connectionDto = ConnectionDto.builder()
                .username("cd@gmail.com").build();

        Optional<Member> optionalMember1 = Optional.of(member1);
        Optional<Member> optionalMember2 = Optional.of(member2);

        //when
        when(memberService.findByUsername(member1.getUsername())).thenReturn(optionalMember1);
        when(memberService.findByUsername(member2.getUsername())).thenReturn(optionalMember2);

        //then
        memberService.addConnection(customUserDetails.getUsername(), connectionDto);
        assertThat(optionalMember1.get().getConnections().contains(optionalMember2.get())).isTrue();
    }

    @Test
    public void addConnectionTestMemberNotExist() {
        //given

        CustomUserDetails customUserDetails = new CustomUserDetails(member1);

        ConnectionDto connectionDto = ConnectionDto.builder()
                .username("cd@gmail.com").build();

        //when
        when(memberService.findByUsername(member1.getUsername())).thenReturn(Optional.empty());
        when(memberService.findByUsername(member2.getUsername())).thenReturn(Optional.of(member2));

        //then
        assertThatIllegalStateException().isThrownBy(()->memberService.addConnection(customUserDetails.getUsername(), connectionDto));
    }

    @Test
    public void addConnectionTestConnectionMemberNotExist() {
        //given

        CustomUserDetails customUserDetails = new CustomUserDetails(member1);

        ConnectionDto connectionDto = ConnectionDto.builder()
                .username("cd@gmail.com").build();

        //when
        when(memberService.findByUsername(member1.getUsername())).thenReturn(Optional.of(member1));
        when(memberService.findByUsername(member2.getUsername())).thenReturn(Optional.empty());

        //then
        assertThatIllegalStateException().isThrownBy(()->memberService.addConnection(customUserDetails.getUsername(), connectionDto));
    }

    @Test
    public void deleteConnectionTest() {
        //given
        member1.getConnections().add(member2);

        CustomUserDetails customUserDetails = new CustomUserDetails(member1);

        String deleteConnectionUsername = member2.getUsername();

        Optional<Member> optionalMember1 = Optional.of(member1);

        //when
        when(memberService.findByUsername(member1.getUsername())).thenReturn(optionalMember1);

        //then
        memberService.deleteConnection(customUserDetails.getUsername(), deleteConnectionUsername);
        assertThat(optionalMember1.get().getConnections().contains(member2)).isFalse();
    }

    @Test
    public void deleteConnectionTestMemberNotExist() {
        //given
        member1.getConnections().add(member2);

        CustomUserDetails customUserDetails = new CustomUserDetails(member1);

        String deleteConnectionUsername = member2.getUsername();

        Optional<Member> optionalMember1 = Optional.of(member1);

        //when
        when(memberService.findByUsername(member1.getUsername())).thenReturn(Optional.empty());

        //then
        assertThatIllegalStateException().isThrownBy(()->memberService.deleteConnection(customUserDetails.getUsername(), deleteConnectionUsername));
    }

    @Test
    public void getAllConnectionsTest() {
        //given
        member1.getConnections().add(member2);

        CustomUserDetails customUserDetails = new CustomUserDetails(member1);

        String deleteConnectionUsername = member2.getUsername();

        Optional<Member> optionalMember1 = Optional.of(member1);

        //when
        when(memberService.findByUsername(member1.getUsername())).thenReturn(optionalMember1);

        //then
        assertThat(memberService.getAllConnections(customUserDetails.getUsername())).isEqualTo(member1.getConnections());
    }

    @Test
    public void getAllConnectionsTestMemberNotExist() {
        //given

        //when
        when(memberService.findByUsername(member1.getUsername())).thenReturn(Optional.empty());

        //then
        assertThatIllegalStateException().isThrownBy(()->memberService.getAllConnections(member1.getUsername()));
    }
}
