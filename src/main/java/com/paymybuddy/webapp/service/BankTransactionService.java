package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.BankTransaction;
import com.paymybuddy.webapp.model.CustomUserDetails;
import com.paymybuddy.webapp.model.Member;
import com.paymybuddy.webapp.repository.BankTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class BankTransactionService {

    @Autowired
    private final BankTransactionRepository bankTransactionRepository;

    @Autowired
    private final MemberService memberService;

    public BankTransactionService(BankTransactionRepository bankTransactionRepository, MemberService memberService) {
        this.bankTransactionRepository = bankTransactionRepository;
        this.memberService = memberService;
    }
    @Transactional
    public boolean sendToBank(CustomUserDetails memberInfo, BigDecimal amount) {
        Optional<Member> optionalMember = memberService.findByUsername(memberInfo.getUsername());
        if(optionalMember.isEmpty()) return false;
        Member member = optionalMember.get();

        BigDecimal amountBefore = member.getAmount();
        if(amountBefore.compareTo(new BigDecimal("2.1")) < 0) {
            throw new IllegalStateException("Should have more than 2,1 euro");
        }

        if(amount.compareTo(new BigDecimal("2.1")) < 0) {
            throw new IllegalStateException("Should send more than 2,1 euro");
        }

        BigDecimal fee = amount.multiply(new BigDecimal("0.005")).setScale(2, RoundingMode.HALF_EVEN);

        if(amountBefore.compareTo(amount.add(fee)) < 0) {
            throw new IllegalStateException("Not enough money");
        }

        member.setAmount(amountBefore.subtract(amount.add(fee)));
        BankTransaction bankTransaction = BankTransaction.builder()
                .amount(amount.multiply(new BigDecimal(-1)))
                .fee(fee)
                .transactionTime(LocalDateTime.now())
                .member(member).build();

        bankTransactionRepository.save(bankTransaction);

        return true;
    }

    @Transactional
    public boolean receiveFromBank(CustomUserDetails memberInfo, BigDecimal amount) {
        Optional<Member> optionalMember = memberService.findByUsername(memberInfo.getUsername());
        if(optionalMember.isEmpty()) return false;
        Member member = optionalMember.get();

        BigDecimal amountBefore = member.getAmount();

        //TODO : REFACTOR
        if(amount.compareTo(new BigDecimal("2.1")) < 0) {
            throw new IllegalStateException("Should send more than 2,1 euro");
        }

        BigDecimal fee = amount.multiply(new BigDecimal("0.005")).setScale(2, RoundingMode.HALF_EVEN);

        if(amountBefore.compareTo(amount.add(fee)) < 0) {
            throw new IllegalStateException("Not enough money");
        }

        member.setAmount(amountBefore.add(amount));
        BankTransaction bankTransaction = BankTransaction.builder()
                .amount(amount)
                .fee(fee)
                .transactionTime(LocalDateTime.now())
                .member(member).build();

        bankTransactionRepository.save(bankTransaction);

        return true;
    }
}
