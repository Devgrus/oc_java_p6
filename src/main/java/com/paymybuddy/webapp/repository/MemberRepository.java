package com.paymybuddy.webapp.repository;

import com.paymybuddy.webapp.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    public Member findByEmail(String email);
}
