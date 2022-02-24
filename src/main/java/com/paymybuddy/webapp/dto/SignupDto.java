package com.paymybuddy.webapp.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SignupDto {

    private String username;
    private String password;
    private String nickname;

}
