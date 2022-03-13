package com.paymybuddy.webapp.dto;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MemberTransactionListDto {
    private String username;
    private String nickname;
    private String description;
    private BigDecimal amount;
}
