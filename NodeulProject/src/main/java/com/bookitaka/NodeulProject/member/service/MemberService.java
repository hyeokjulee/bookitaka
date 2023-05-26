package com.bookitaka.NodeulProject.member.service;

import com.bookitaka.NodeulProject.member.dto.MemberChangePwDTO;
import com.bookitaka.NodeulProject.member.dto.MemberUpdateDTO;
import com.bookitaka.NodeulProject.member.dto.UserResponseDTO;
import com.bookitaka.NodeulProject.member.exception.CustomException;
import com.bookitaka.NodeulProject.member.model.Member;
import com.bookitaka.NodeulProject.member.repository.MemberRepository;
import com.bookitaka.NodeulProject.member.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
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
        if (!memberRepository.existsByMemberEmail(member.getMemberEmail())) {
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

        if (!memberRepository.existsByMemberEmail(member.getMemberEmail())) {
            if (member.getMemberPassword().equals(oldPw)) {
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

    public List<String> getAllMemberEmail(String memberName, String memberBirthday) {
        List<Member> findMember = memberRepository.findByMemberName(memberName);
        List<String> mList = new ArrayList<String>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        if (findMember != null) {
            for (Member member : findMember) {
                if (dateFormat.format(member.getMemberBirthday()).equals(memberBirthday)) {
                    mList.add(member.getMemberEmail());
                }
            }
            return mList;
        } else {
            throw new CustomException("The member doesn't exist", HttpStatus.NOT_FOUND);
        }
    }

    public void getPwByEMail(String memberEmail, String memberName) {
        if (memberRepository.existsByMemberEmail(memberEmail)) {
            Member findMember = memberRepository.findByMemberEmail(memberEmail);
            if (findMember.getMemberName().equals(memberName)) {
                // 메일전송
            }
        } else {
            throw new CustomException("The member doesn't exist", HttpStatus.NOT_FOUND);
        }
    }

    public List<Member> getAllMembers() {
        List<Member> members = memberRepository.findAll();
        return members;
    }

}


