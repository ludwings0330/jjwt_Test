package com.example.jwttest2.controller;

import com.example.jwttest2.dto.MemberDto;
import com.example.jwttest2.model.Member;
import com.example.jwttest2.service.JwtService;
import com.example.jwttest2.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin("*")
@Slf4j
@RequiredArgsConstructor
public class HomeController {
    private final MemberService memberService;
    private final JwtService jwtService;
    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public Member login(@ModelAttribute MemberDto member) {
        log.info("{} {}", member);
        final Member pass = memberService.login(member);
        if(pass != null) {
            final String accessToken = jwtService.createAccessToken("userInfo", pass);
            final String refreshToken = jwtService.createRefreshToken("userInfo", pass);

            log.info("created access token ::: {}", accessToken);
            log.info("created refresh token ::: {}", refreshToken);
            return pass;
        }
        return null;
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody MemberDto member) {
        log.info("{}", member);
        final Member join = memberService.join(member);
        log.info("{}", join);
        return new ResponseEntity<>("OK!", HttpStatus.OK);
    }

    @GetMapping("/check")
    public String checkToken(@RequestHeader Map<String, String> data) {
        log.info("data : [{}]", data);
        final String accessToken = data.get("access-token");
        if(jwtService.checkToken(accessToken)) {
            log.info("사용 가능한 access token");
            return "OK";
        }  else {
            log.info("만료된 access token");
            return "FAIL";
        }
    }

    @GetMapping("/refresh")
    public String refreshToken(@RequestHeader("refresh-token") String refreshToken) {
        if(jwtService.checkToken(refreshToken)) {
            log.info("사용 가능한 refresh token");
            final String accessToken = jwtService.createAccessToken("userInfo", null);
            log.info("access token 재발급 완료 ::: {}", accessToken);
            return "ok";
        } else {
            log.info("만료된 refresh token 으로 재인증 필요");
            return "UNAUTHORIZED";
        }
    }
}
