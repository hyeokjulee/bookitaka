package com.bookitaka.NodeulProject.member.service;

import com.bookitaka.NodeulProject.member.dto.MemberFindEmailDTO;
import com.bookitaka.NodeulProject.member.dto.MemberChangePwDTO;
import com.bookitaka.NodeulProject.member.exception.CustomException;
import com.bookitaka.NodeulProject.member.mail.EmailSender;
import com.bookitaka.NodeulProject.member.model.Member;
import com.bookitaka.NodeulProject.member.model.MemberRoles;
import com.bookitaka.NodeulProject.member.repository.MemberRepository;
import com.bookitaka.NodeulProject.member.security.JwtTokenProvider;
import com.bookitaka.NodeulProject.member.security.Token;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final EmailSender emailService;

    /************************************************************************************************
     * Member Service
     ************************************************************************************************/

    @Transactional
    public Map<String, String> signin(String memberEmail, String memberPassword) {
        try {
            // 회원 로그인 인증
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(memberEmail, memberPassword));
            // access 토큰 발급 - HTTP-only 쿠키 저장
            String aToken = jwtTokenProvider.createToken(memberEmail, memberRepository.findByMemberEmail(memberEmail).getMemberRole());
            // refresh 토큰 발급 - HTTP-only 쿠키 및 회원DB 저장
            String rToken = jwtTokenProvider.createRefreshToken(memberEmail, memberRepository.findByMemberEmail(memberEmail).getMemberRole());
            Member member = memberRepository.findByMemberEmail(memberEmail);
            // Member DB에 저장
            member.setMemberRtoken(rToken);
            memberRepository.save(member);
            // 반환 Map객체에 두 토큰 저장
            Map<String, String> tokens = new HashMap<>();
            tokens.put(Token.ACCESS_TOKEN, aToken);
            tokens.put(Token.REFRESH_TOKEN, rToken);

            return tokens;

        } catch (AuthenticationException e) {
            throw new CustomException("Invalid email/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public boolean signout(String memberEmail) {
        Member member = memberRepository.findByMemberEmail(memberEmail);
        if (member != null) {
            member.setMemberRtoken(null);
            memberRepository.save(member);
            return true;
        }
        return false;
    }

    public boolean signup(Member member) {
        if (!memberRepository.existsByMemberEmail(member.getMemberEmail())) {
            member.setMemberPassword(passwordEncoder.encode(member.getMemberPassword()));
            member.setMemberRole(MemberRoles.MEMBER);
            memberRepository.save(member);
            return true;
        } else {
            throw new CustomException("Member email is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public void delete(String memberEmail) {
        memberRepository.deleteByMemberEmail(memberEmail);
    }

    public Member search(String memberEmail) {
        Member member = memberRepository.findByMemberEmail(memberEmail);
        log.info("================================ memberEmail : {}", memberEmail);
        if (member == null) {
            throw new CustomException("The member doesn't exist", HttpStatus.NOT_FOUND);
        }
        return member;
    }

    public Member whoami(Cookie[] cookies, String tokenCookieName) {
        return memberRepository.findByMemberEmail(jwtTokenProvider.getMemberEmail(jwtTokenProvider.resolveToken(cookies, tokenCookieName)));
    }

    public boolean modifyMember(Member member) {
        if (memberRepository.existsByMemberEmail(member.getMemberEmail())) {
            memberRepository.save(member);
            return true;
        } else {
            throw new CustomException("The member doesn't exist", HttpStatus.NOT_FOUND);
        }
    }

    public int modifyPassword(Member member, MemberChangePwDTO memberChangePwDTO) {
        String oldPw = memberChangePwDTO.getOldMemberPassword();
        String newPw = memberChangePwDTO.getNewMemberPassword();
        String newPwChk = memberChangePwDTO.getNewMemberPasswordCheck();

        if (member != null && passwordEncoder.matches(oldPw, member.getMemberPassword())) {
            if (passwordEncoder.matches(newPw, member.getMemberPassword())) {
                return 1;
            }
            
            if (newPw.equals(newPwChk)) {
                member.setMemberPassword(passwordEncoder.encode(newPw));
                memberRepository.save(member);
                return 0;
            }

        }
        return 2;
    }


    public List<String> getMemberEmail(MemberFindEmailDTO memberFindEmailDTO) {
        String memberName = memberFindEmailDTO.getMemberName();
        String memberBirthday = memberFindEmailDTO.getMemberBirthday();
        List<Member> findMember = memberRepository.findByMemberName(memberName);
        List<String> mList = null;
        log.info("====================== dto : {}", memberBirthday);

        if (findMember != null && findMember.size() > 0) {
            mList = new ArrayList<>();

            for (Member member : findMember) {
                log.info("====================== member : {}", member.getMemberBirthday());
                log.info("{}", member.getMemberBirthday().equals(memberBirthday));

                if (member.getMemberBirthday().equals(memberBirthday)) {
                    mList.add(member.getMemberEmail());
                    log.info("========================================= mList : {}", mList);
                }
            }
            if (mList.size() == 0) {
                mList = null;
            }
        }
        return mList;
    }

    public boolean getPwByEmailAndName(String memberEmail, String memberName) {
        if (memberRepository.existsByMemberEmailAndMemberName(memberEmail, memberName)) {
            String newPw = generateRandomPassword();
            // 메일전송
            if (emailService.sendEmailWithTemplate(memberEmail, "templates/member/temp-password-email-template.html", "[북키타카] 임시비밀번호 발송", newPw)) {
                // 메일전송 성공 시
                // 임시 비밀번호로 비밀번호 변경 후 디비에 저장
                log.info("================================== newPw : {}", newPw);
                Member member = memberRepository.findByMemberEmail(memberEmail);
                member.setMemberPassword(passwordEncoder.encode(newPw));
                memberRepository.save(member);
                return true;
            }
        }
        return false;
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
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


    /************************************************************************************************
     * JWT Service
     ************************************************************************************************/

    public boolean isValidToken(Cookie[] cookies) {
        String token = jwtTokenProvider.resolveToken(cookies, Token.ACCESS_TOKEN);
        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                return true;
            }
        } catch (CustomException ex) {
            return false;
        }
        return false;
    }

    public String refresh(Cookie[] cookies, Member member) {
        String req_rToken = jwtTokenProvider.resolveToken(cookies, Token.REFRESH_TOKEN);
        String db_rToken = member.getMemberRtoken();
        if (req_rToken != null && req_rToken.equals(db_rToken)) {
            return jwtTokenProvider.createToken(member.getMemberEmail(), member.getMemberRole());
        }
        return null;
    }
}
