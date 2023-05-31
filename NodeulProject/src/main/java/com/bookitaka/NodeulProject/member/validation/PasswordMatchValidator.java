package com.bookitaka.NodeulProject.member.validation;

import com.bookitaka.NodeulProject.member.dto.UserDataDTO;
import org.apache.commons.beanutils.PropertyUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, Object> {
    private String passwordFieldName;
    private String pwChkField;

    @Override
    public void initialize(PasswordMatch constraintAnnotation) {
        passwordFieldName = constraintAnnotation.passwordField();
        pwChkField = constraintAnnotation.passwordConfirmField();
    }


    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            String password = (String) PropertyUtils.getProperty(value, passwordFieldName);
            String pwChk = (String) PropertyUtils.getProperty(value, pwChkField);

            return password != null && password.equals(pwChk);
        } catch (Exception e) {
            // 필드 값을 가져올 수 없는 경우 기본적으로 유효하다고 판단
            return true;
        }
    }
}
