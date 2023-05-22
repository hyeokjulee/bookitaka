package com.bookitaka.NodeulProject.member.bstest.repository;

import com.bookitaka.NodeulProject.member.bstest.model.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class MemberRepositoryTest {

    @PersistenceContext
    private EntityManager em;

    public void save(Member member){em.persist(member);}

    public Member findOne(Integer id){
        return em.find(Member.class, id);
    }

    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    public List<Member> findByMemberEmail(String name){
        return em.createQuery("select m from Member m where m.memberEmail = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

}
