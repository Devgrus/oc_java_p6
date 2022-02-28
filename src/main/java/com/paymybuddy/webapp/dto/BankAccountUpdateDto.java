package com.paymybuddy.webapp.dto;

import com.paymybuddy.webapp.model.CustomUserDetails;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BankAccountUpdateDto {
    private CustomUserDetails customUserDetails;
    private String bankAccount;
}
