package com.bookitaka.NodeulProject.member.bstest.service;

import com.bookitaka.NodeulProject.member.bstest.model.Member;
import com.bookitaka.NodeulProject.member.bstest.repository.MemberRepositoryBackup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor //final이 있는 속성들만 생성자에 자동적으로 넣어주는 Lombok annotation
public class MemberServiceBackup {

    /*@Autowired
    private MemberRepository memberRepository;*/

    //좀 더 나은 Injection 방법 Constructor Injection
    private final MemberRepositoryBackup memberRepository; //final을 넣으면 컴파일 시점에 주입이 제대로 되는지 확인이 가능하다.

    /**
     * 회원 가입
     * @param member
     * @return
     */
    @Transactional
    public Integer join(Member member){
        validateDuplicateMember(member); //중복 회원 검사
        memberRepository.save(member);
        return member.getMemberNo();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByMemberEmail(member.getMemberEmail());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
        //Exception
    }

    //회원 전체 조회
    @Transactional(readOnly = true) //조회만 하는 service에서 readOnly설정을 해주면 좀 더 최적화와 성능개선을 기대할 수 있다.
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Member findOne(Integer memberId){
        return memberRepository.findOne(memberId);
    }
}