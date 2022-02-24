package com.paymybuddy.webapp.repository;

import com.paymybuddy.webapp.model.Role;
import com.paymybuddy.webapp.model.Member;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void findUserByEmailTest() {
        //given
        Member user1 = Member.builder()
                .id(1L)
                .username("abc@email.com")
                .password("1111")
                .nickname("user1")
                .role(Role.GUEST)
                .bankAccount("111-111-1111")
                .amount(BigDecimal.valueOf(1000))
                .connections(new HashSet<>())
                .banktransitions(new ArrayList<>())
                .usertransitions(new ArrayList<>())
                .build();

        //when
        memberRepository.save(user1);

        //then

        assertThat(memberRepository.findByUsername("abc@email.com").get().getId()).isEqualTo(1L);
    }
}
