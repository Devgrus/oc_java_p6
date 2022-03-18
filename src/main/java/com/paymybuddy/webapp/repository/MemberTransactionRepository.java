package com.paymybuddy.webapp.repository;

import com.paymybuddy.webapp.model.Member;
import com.paymybuddy.webapp.model.MemberTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberTransactionRepository extends JpaRepository<MemberTransaction, Long> {
    Page<MemberTransaction> findByRemitterOrderByTransactionTimeDesc(Member member, Pageable pageable);
}
