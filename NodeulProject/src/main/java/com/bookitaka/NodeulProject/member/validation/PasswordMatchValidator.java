package com.bookitaka.NodeulProject.member.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, Object> {

    private String passwordFieldName;
    private String passwordCheckFieldName;

    @Override
    public void initialize(PasswordMatch constraintAnnotation) {
        passwordFieldName = constraintAnnotation.password();
        passwordCheckFieldName = constraintAnnotation.passwordCheck();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        try {
            String password = getValue(object, passwordFieldName);
            String passwordCheck = getValue(object, passwordCheckFieldName);

            return password != null && password.equals(passwordCheck);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            // Handle exceptions if necessary
            return false;
        }
    }

    private String getValue(Object object, String fieldName)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String methodName = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
        Method method = object.getClass().getMethod(methodName);
        return (String) method.invoke(object);
    }
}
