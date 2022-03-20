package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.BankTransaction;
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

    private final BankTransactionRepository bankTransactionRepository;
    private final MemberService memberService;

    @Autowired
    public BankTransactionService(BankTransactionRepository bankTransactionRepository, MemberService memberService) {
        this.bankTransactionRepository = bankTransactionRepository;
        this.memberService = memberService;
    }

    public boolean isBankAccountExist(String username) {
        Optional<Member> optionalMember = memberService.findByUsername(username);
        if(optionalMember.isEmpty()) throw new IllegalStateException("User not found");
        return optionalMember.get().getBankAccount() != null;
    }

    @Transactional
    public void sendToBank(String username, BigDecimal amount) {
        Optional<Member> optionalMember = memberService.findByUsername(username);
        if(optionalMember.isEmpty()) throw new IllegalStateException("User not found");

        Member member = optionalMember.get();

        BigDecimal amountBefore = member.getAmount();

        isMoreThanMinimumTransactionAmount(amount);

        BigDecimal fee = calculateFee(amount);

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
    }

    @Transactional
    public void receiveFromBank(String username, BigDecimal amount) {
        Optional<Member> optionalMember = memberService.findByUsername(username);
        if(optionalMember.isEmpty()) throw new IllegalStateException("User not found");

        Member member = optionalMember.get();

        BigDecimal amountBefore = member.getAmount();

        isMoreThanMinimumTransactionAmount(amount);

        BigDecimal fee = calculateFee(amount);

        member.setAmount(amountBefore.add(amount));
        BankTransaction bankTransaction = BankTransaction.builder()
                .amount(amount)
                .fee(fee)
                .transactionTime(LocalDateTime.now())
                .member(member).build();

        bankTransactionRepository.save(bankTransaction);
    }

    private void isMoreThanMinimumTransactionAmount(BigDecimal amount) {
        if(amount.compareTo(new BigDecimal("1")) < 0) throw new IllegalStateException("Minimum 1€");
    }

    private BigDecimal calculateFee(BigDecimal amount) {
        return amount.compareTo(new BigDecimal("2.1")) >= 0 ?
                amount.multiply(new BigDecimal("0.005")).setScale(2, RoundingMode.HALF_EVEN) :
                new BigDecimal("0.01");
    }
}
