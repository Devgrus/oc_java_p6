package com.paymybuddy.webapp.dto;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MemberTransactionDto {
    private String receiver;
    private String description;
    private BigDecimal amount;
}
