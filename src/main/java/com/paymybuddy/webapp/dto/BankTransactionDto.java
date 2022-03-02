package com.paymybuddy.webapp.dto;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BankTransactionDto {
    private BigDecimal amount;
}
