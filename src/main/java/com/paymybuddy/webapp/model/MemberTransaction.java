package com.paymybuddy.webapp.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_member")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private BigDecimal amount;

    @Column
    private BigDecimal fee;

    @Column(name = "transaction_time")
    private LocalDateTime transitionTime;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "remitter_id")
    private Member remitter;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Member receiver;

}
