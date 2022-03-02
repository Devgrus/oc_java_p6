package com.paymybuddy.webapp.config;

import com.paymybuddy.webapp.model.Role;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.math.BigDecimal;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithUserDetailsSecurityContextFactory.class)
public @interface WithMockCustomUser {

    String username() default "test@abc.com";

    String password() default "1234";

    String nickname() default "testuser";

    Role role() default Role.USER;

    String bankAccount() default "11-111-111";

    String amount() default "1000";
}
