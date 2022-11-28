package com.example.jwttest2.service;

import com.example.jwttest2.dto.MemberDto;
import com.example.jwttest2.model.Member;

public interface MemberService {
    Member join(MemberDto member);
    Member login(MemberDto member);
    String encryptPassword(String plainPwd);
    boolean checkPwd(String plainPwd, String hashPwd);
}
