package com.bookitaka.NodeulProject.member.service;

import com.bookitaka.NodeulProject.member.dto.MemberChangePwDTO;
import com.bookitaka.NodeulProject.member.dto.MemberUpdateDTO;
import com.bookitaka.NodeulProject.member.dto.UserResponseDTO;
import com.bookitaka.NodeulProject.member.exception.CustomException;
import com.bookitaka.NodeulProject.member.model.Member;
import com.bookitaka.NodeulProject.member.repository.MemberRepository;
import com.bookitaka.NodeulProject.member.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public String signin(String memberEmail, String memberPassword) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(memberEmail, memberPassword));
            return jwtTokenProvider.createToken(memberEmail, memberRepository.findByMemberEmail(memberEmail).getMemberRole());
        } catch (AuthenticationException e) {
            throw new CustomException("Invalid email/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public String signup(Member member) {
        if (!memberRepository.existsByMemberEmail(member.getMemberEmail())) {
            member.setMemberPassword(passwordEncoder.encode(member.getMemberPassword()));
            memberRepository.save(member);
            return jwtTokenProvider.createToken(member.getMemberEmail(), member.getMemberRole());
        } else {
            throw new CustomException("Member email is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public void delete(String memberEmail) {
        memberRepository.deleteByMemberEmail(memberEmail);
    }

    public Member search(String memberEmail) {
        Member member = memberRepository.findByMemberEmail(memberEmail);
        if (member == null) {
            throw new CustomException("The member doesn't exist", HttpStatus.NOT_FOUND);
        }
        return member;
    }

    public Member whoami(HttpServletRequest req) {
        return memberRepository.findByMemberEmail(jwtTokenProvider.getMemberEmail(jwtTokenProvider.resolveToken(req)));
    }

    public Boolean checkToken(HttpServletRequest req) {
        String token = jwtTokenProvider.resolveToken(req);
        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                return true;
            }
        } catch (CustomException ex) {
            return false;
        }
        return false;
    }

    public String refresh(String memberEmail) {
        return jwtTokenProvider.createToken(memberEmail, memberRepository.findByMemberEmail(memberEmail).getMemberRole());
    }

    public boolean modifyMember(Member member, MemberUpdateDTO memberUpdateDTO) {
        if (memberRepository.existsByMemberEmail(member.getMemberEmail())) {
            member.setMemberName(memberUpdateDTO.getMemberName());
            member.setMemberGender(memberUpdateDTO.getMemberGender());
            member.setMemberPhone(memberUpdateDTO.getMemberPhone());
            member.setMemberBirthday(memberUpdateDTO.getMemberBirthday());
            memberRepository.save(member);
            return true;
        } else {
            throw new CustomException("The member doesn't exist", HttpStatus.NOT_FOUND);
        }
    }

    public boolean modifyPassword(Member member, MemberChangePwDTO memberChangePwDTO) {
        String oldPw = memberChangePwDTO.getOldPw();
        String newPw = memberChangePwDTO.getNewPw();
        String newPwChk = memberChangePwDTO.getNewPwChk();

        if (memberRepository.existsByMemberEmail(member.getMemberEmail())) {
            if (passwordEncoder.matches(oldPw, member.getMemberPassword())) {
                if (newPw.equals(newPwChk)) {
                    member.setMemberPassword(passwordEncoder.encode(newPw));
                    memberRepository.save(member);
                    return true;
                } else {
                    throw new CustomException("wrong pwChk", HttpStatus.UNPROCESSABLE_ENTITY);
                }
            } else {
                throw new CustomException("wrong password", HttpStatus.UNPROCESSABLE_ENTITY);
            }
        } else {
            throw new CustomException("wrong member", HttpStatus.NOT_FOUND);
        }
    }

    public List<String> getMemberEmail(String memberName, String memberBirthday) {
        List<Member> findMember = memberRepository.findByMemberName(memberName);
        List<String> mList = new ArrayList<String>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        if (findMember != null) {
            for (Member member : findMember) {
                if (dateFormat.format(member.getMemberBirthday()).equals(memberBirthday)) {
                    mList.add(member.getMemberEmail());
                }
            }
            if (mList.isEmpty()) {
                throw new CustomException("wrong birthday", HttpStatus.NOT_FOUND);
            }
            return mList;
        } else {
            throw new CustomException("wrong memberName", HttpStatus.NOT_FOUND);
        }
    }

    public void getPwByEmail(String memberEmail, String memberName) {
        if (memberRepository.existsByMemberEmail(memberEmail)) {
            Member findMember = memberRepository.findByMemberEmail(memberEmail);
            if (findMember.getMemberName().equals(memberName)) {
                String newPw = generateRandomPassword();

                /*
                *
                *
                *                   메일전송
                *
                *
                * */

                // 임시 비밀번호로 비밀번호 변경
                findMember.setMemberPassword(passwordEncoder.encode(newPw));
                memberRepository.save(findMember);

            }
        } else {
            throw new CustomException("The member doesn't exist", HttpStatus.NOT_FOUND);
        }
    }

    public List<Member> getAllMembers() {
        List<Member> members = memberRepository.findAll();
        return members;
    }

    private String generateRandomPassword() {
        // 임의의 비밀번호 생성 로직을 구현한다.
        // 예를 들어, 랜덤한 문자열을 생성하거나 암호화 알고리즘을 활용할 수 있다.
        // 이 예시에서는 간단하게 8자리의 랜덤한 숫자와 문자 조합으로 비밀번호를 생성하는 방식을 사용한다.
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        int passwordLength = 8;

        for (int i = 0; i < passwordLength; i++) {
            int index = random.nextInt(characters.length());
            password.append(characters.charAt(index));
        }

        return password.toString();
    }

}


