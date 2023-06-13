package com.bookitaka.NodeulProject.member.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class RoleMemberValidator implements ConstraintValidator<RoleMember, String> {

    private static final Pattern ROLE_MEMBER_PATTERN = Pattern.compile("ROLE_MEMBER");
    private static final Pattern ROLE_ADMIN_PATTERN = Pattern.compile("ROLE_ADMIN");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return ROLE_MEMBER_PATTERN.matcher(value).matches() || ROLE_ADMIN_PATTERN.matcher(value).matches() ;
    }
}
