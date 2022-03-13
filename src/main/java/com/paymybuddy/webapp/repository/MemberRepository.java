package com.paymybuddy.webapp.repository;

import com.paymybuddy.webapp.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String email);
    Optional<Member> findById(Long id);
}
