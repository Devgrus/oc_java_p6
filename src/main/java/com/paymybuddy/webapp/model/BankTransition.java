package com.paymybuddy.webapp.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transition_bank")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankTransition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private BigDecimal amount;

    @Column
    private BigDecimal fee;

    @Column(name = "transition_time")
    private LocalDateTime transitionTime;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
