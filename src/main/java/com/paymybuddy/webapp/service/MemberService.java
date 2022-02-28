package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.BankAccountUpdateDto;
import com.paymybuddy.webapp.dto.SignupDto;
import com.paymybuddy.webapp.model.Member;
import com.paymybuddy.webapp.model.Role;
import com.paymybuddy.webapp.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Member save(SignupDto signupDto) {
        Optional<Member> member = memberRepository.findByUsername(signupDto.getUsername());
        if(member.isPresent()) {
            throw new IllegalStateException("User already exist");
        }
        Member newMember = Member.builder()
                .username(signupDto.getUsername())
                .password(passwordEncoder.encode(signupDto.getPassword()))
                .nickname(signupDto.getNickname())
                .role(Role.GUEST)
                .bankAccount(null)
                .amount(BigDecimal.valueOf(0))
                .connections(new HashSet<>())
                .banktransitions(new ArrayList<>())
                .usertransitions(new ArrayList<>())
                .build();

        return memberRepository.save(newMember);
    }

    public boolean addBankAccount(BankAccountUpdateDto bankAccountUpdateDto) {
        Optional<Member> optionalMember = memberRepository.findByUsername(bankAccountUpdateDto.getCustomUserDetails().getUsername());
        if(optionalMember.isEmpty()) {
            return false;
        }

//        member.get().setBankAccount(bankAccountUpdateDto.getBankAccount());
        optionalMember.ifPresent(i-> i.setBankAccount(bankAccountUpdateDto.getBankAccount()));

        return true;
    }

    public Optional<Member> findByUsername(String email) {
        return memberRepository.findByUsername(email);
    }
}
