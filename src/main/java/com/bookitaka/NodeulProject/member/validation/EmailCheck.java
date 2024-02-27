package com.bookitaka.NodeulProject.member.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailCheckValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailCheck {
    String message() default "이미 사용중인 이메일 입니다.2";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

