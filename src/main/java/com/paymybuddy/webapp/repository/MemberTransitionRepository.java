package com.paymybuddy.webapp.repository;

import com.paymybuddy.webapp.model.MemberTransition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberTransitionRepository extends JpaRepository<MemberTransition, Long> {
}
