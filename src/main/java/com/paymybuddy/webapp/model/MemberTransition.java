package com.paymybuddy.webapp.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transition_member")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberTransition {

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
    @JoinColumn(name = "remitter")
    private Member remitter;

    @ManyToOne
    @JoinColumn(name = "receiver")
    private Member receiver;

}
