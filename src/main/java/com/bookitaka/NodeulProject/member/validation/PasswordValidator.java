package com.bookitaka.NodeulProject.member.validation;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class PasswordValidator implements ConstraintValidator<Password, String> {

    private String[] patterns;
    private String[] messages;

    @Override
    public void initialize(Password constraintAnnotation) {
        this.patterns = constraintAnnotation.passwordPatterns();
        this.messages = constraintAnnotation.passwordMessages();
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        log.info("====== isValid : password : {}", password);
        if (password == null || password.isBlank()) {
            return true; // 빈 문자열이면 유효하게 처리
        }
        log.info("=====isValid11 {}", password.matches(patterns[0]));
        // 8자리 이하일 경우
        if (!password.matches(patterns[0])) {
            log.info("=====isValid1 {}", messages[0]);
            setCustomMessage(context, messages[0]);
            return false;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i < patterns.length; i++) {
            log.info("=====isValid22 {}", password.matches(patterns[i]));
            String pattern = patterns[i];
            String message = messages[i];
            if (!password.matches(patterns[i])) {
                stringBuilder.append(message).append(", ");
            }
        }

        if (!stringBuilder.toString().isBlank()) {
            String msg = "비밀번호는 적어도 하나의 " + stringBuilder.substring(0, stringBuilder.toString().length() - 2) + "를 포함해야 합니다.";
            log.info("=====isValid2 {}", msg);
            setCustomMessage(context, msg);
            return false;
        }
        return true;
    }

    private void setCustomMessage(ConstraintValidatorContext context, String messages) {
        context.disableDefaultConstraintViolation(); // 기본 메시지 사용 비활성화
        context.buildConstraintViolationWithTemplate(messages)
                .addConstraintViolation(); // 커스텀 메시지 설정
    }
}

