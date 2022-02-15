package com.paymybuddy.webapp.repository;

import com.paymybuddy.webapp.model.BankTransition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankTransitionRepository extends JpaRepository<BankTransition, Long> {
}
