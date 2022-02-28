package com.paymybuddy.webapp.repository;

import com.paymybuddy.webapp.model.MemberTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberTransactionRepository extends JpaRepository<MemberTransaction, Long> {
}
