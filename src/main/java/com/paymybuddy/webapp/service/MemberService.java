package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.BankAccountUpdateDto;
import com.paymybuddy.webapp.dto.ConnectionDto;
import com.paymybuddy.webapp.dto.SignupDto;
import com.paymybuddy.webapp.model.Member;
import com.paymybuddy.webapp.model.Role;
import com.paymybuddy.webapp.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;

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
        optionalMember.ifPresent(i-> {
            i.setBankAccount(bankAccountUpdateDto.getBankAccount());
            i.setRole(Role.USER);
                });

        return true;
    }

    @Transactional
    public Optional<Member> findByUsername(String email) {
        return memberRepository.findByUsername(email);
    }

    @Transactional
    public Optional<Member> findById(Long id) { return memberRepository.findById(id); }

    @Transactional
    public void addConnection(String username, ConnectionDto connectionDto) {
        Optional<Member> optionalMember = findByUsername(username);
        Optional<Member> optionalConnectionMember = findByUsername(connectionDto.getUsername());
        if(optionalMember.isEmpty() || optionalConnectionMember.isEmpty()) throw new IllegalStateException("User not found");

        Member member = optionalMember.get();

        if(member.getId() == optionalConnectionMember.get().getId()) throw new IllegalStateException("Can not add yourself");

        member.getConnections().add(optionalConnectionMember.get());
    }

    @Transactional
    public void deleteConnection(String username, String connectionUsername) {
        Optional<Member> optionalMember = findByUsername(username);
        if(optionalMember.isEmpty()) throw new IllegalStateException("Invalid user");

        Member member = optionalMember.get();
        member.getConnections().stream()
                .filter(i-> Objects.equals(i.getUsername(), connectionUsername)).findFirst()
                .ifPresent(member.getConnections()::remove);
    }

    public Set<Member> getAllConnections(String username) {
        Optional<Member> optionalMember = findByUsername(username);
        if(optionalMember.isEmpty()) throw new IllegalStateException("User not exist");
        return optionalMember.get().getConnections();
    }
}
