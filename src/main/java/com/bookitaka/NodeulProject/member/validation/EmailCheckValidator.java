package com.bookitaka.NodeulProject.member.validation;

import com.bookitaka.NodeulProject.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
@RequiredArgsConstructor
public class EmailCheckValidator implements ConstraintValidator<EmailCheck, String> {

    private final MemberRepository memberRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {

        if (memberRepository.existsByMemberEmail(email)) {
            log.info("=====email {}", email);
            return false;
        }
        return true;
    }

}