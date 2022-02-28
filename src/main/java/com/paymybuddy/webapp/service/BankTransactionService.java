package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.CustomUserDetails;
import com.paymybuddy.webapp.repository.BankTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;

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
    public boolean sendToBank(CustomUserDetails member, BigDecimal amount) {

    }

    public boolean getFromBank() {

    }
}
