package com.example.jwttest2.service;

import com.example.jwttest2.dto.MemberDto;
import com.example.jwttest2.model.Member;
import com.example.jwttest2.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    @Override
    @Transactional
    public Member join(MemberDto member) {
        final Member find = memberRepository.findByName(member.getName());
        if (find != null) {
            log.error("{}는 이미 존재하는 아이디 입니다.", member.getName());
            return find;
        }

        final Member newMember = new Member();
        newMember.setName(member.getName());
        newMember.setPwd(encryptPassword(member.getPwd()));

        memberRepository.save(newMember);
        log.info("회원가입 완료");
        log.info("{}", newMember);
        return newMember;
    }

    @Override
    public Member login(MemberDto member) {
        final Member find = memberRepository.findByName(member.getName());

        log.info("input pwd : {}", member.getPwd());
        log.info("find pwd : {} ", find.getPwd());

        if(checkPwd(member.getPwd(), find.getPwd())) {
            return find;
        }
        return null;
    }

    @Override
    public String encryptPassword(String plainPwd) {
        return BCrypt.hashpw(plainPwd, BCrypt.gensalt());
    }

    @Override
    public boolean checkPwd(String plainPwd, String hashPwd) {
        return BCrypt.checkpw(plainPwd, hashPwd);
    }


}
