package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.MemberTransactionDto;
import com.paymybuddy.webapp.dto.MemberTransactionListDto;
import com.paymybuddy.webapp.model.Member;
import com.paymybuddy.webapp.model.MemberTransaction;
import com.paymybuddy.webapp.repository.MemberTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class MemberTransactionService {

    private final MemberService memberService;
    private final MemberTransactionRepository memberTransactionRepository;

    @Autowired
    public MemberTransactionService(MemberService memberService, MemberTransactionRepository memberTransactionRepository) {
        this.memberService = memberService;
        this.memberTransactionRepository = memberTransactionRepository;
    }

    @Transactional
    public void remit(String remitterUsername, MemberTransactionDto memberTransactionDto) {
        Optional<Member> optionalRemitter = memberService.findByUsername(remitterUsername);
        Optional<Member> optionalReceiver = memberService.findByUsername(memberTransactionDto.getReceiver());
        if(optionalRemitter.isEmpty()) throw new IllegalStateException("User Not Found");
        if(optionalReceiver.isEmpty()) throw new IllegalStateException("Receiver Not Found");

        Member remitter = optionalRemitter.get();
        Member receiver = optionalReceiver.get();

        if(remitter.getId() == receiver.getId()) throw new IllegalStateException("Remitter can not be receiver");

        BigDecimal amountBefore = remitter.getAmount();

        if(memberTransactionDto.getAmount().compareTo(new BigDecimal("1")) < 0) {
            throw new IllegalStateException("Minimum 1€");
        }

        BigDecimal fee = memberTransactionDto.getAmount().compareTo(new BigDecimal("2.1")) >= 0 ?
                memberTransactionDto.getAmount().multiply(new BigDecimal("0.005")).setScale(2, RoundingMode.HALF_EVEN) :
                new BigDecimal("0.01");

        if(amountBefore.compareTo(memberTransactionDto.getAmount().add(fee)) < 0) {
            throw new IllegalStateException("Not enough money");
        }

        remitter.setAmount(amountBefore.subtract(memberTransactionDto.getAmount().add(fee)));
        receiver.setAmount(receiver.getAmount().add(memberTransactionDto.getAmount()));

        MemberTransaction memberTransaction = MemberTransaction.builder()
                .amount(memberTransactionDto.getAmount())
                .fee(fee)
                .transactionTime(LocalDateTime.now())
                .description(memberTransactionDto.getDescription())
                .remitter(remitter)
                .receiver(receiver)
                .build();

        memberTransactionRepository.save(memberTransaction);
    }

    public Page<MemberTransactionListDto> transactionList(String username, Pageable pageable) {
        Optional<Member> optionalMember = memberService.findByUsername(username);
        if(optionalMember.isEmpty()) throw new IllegalStateException("User Not Found");
        Member member = optionalMember.get();

        Page<MemberTransaction> memberTransactions = memberTransactionRepository.findByRemitterOrderByTransactionTimeDesc(member, pageable);

        return memberTransactions.map(i-> MemberTransactionListDto.builder()
                .amount(i.getAmount())
                .description(i.getDescription())
                .username(memberService.findById(i.getReceiver().getId()).isPresent() ?
                        memberService.findById(i.getReceiver().getId()).get().getUsername() :
                        "Unknown User")
                .build());
    }

}
