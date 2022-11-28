package com.example.jwttest2.service;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {
    private static final String SALT = "my_Private_Secret_have_to_over_256_bit_ludwings_jwt_test";
    private static final int ACCESS_TOKEN_EXPIRE_MINUTES = 1;
    private static final int REFRESH_TOKEN_EXPIRE_MINUTES = 2;

    @Override
    public <T> String createAccessToken(String key, T data) {
        return create(key, data, "access-token", 1000 * 60 * ACCESS_TOKEN_EXPIRE_MINUTES);
    }

    @Override
    public <T> String createRefreshToken(String key, T data) {
        return create(key, data, "refresh-token", 1000 * 60 * REFRESH_TOKEN_EXPIRE_MINUTES);
    }

    @Override
    public <T> String create(String key, T data, String subject, long expire) {
        final String jwt = Jwts.builder()
                .setHeaderParam("typ", "JWT").setHeaderParam("regDate", System.currentTimeMillis())
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                .setSubject(subject)
                .claim(key, data)
                .signWith(SignatureAlgorithm.HS256, this.generateKey())
                .compact();

        return jwt;
    }

    // signature 설정에 사용될 key 는 string type 이아니라 byte 배열을 넣어주어야한다.
    private byte[] generateKey() {
        byte[] key = null;
        try {
            key = SALT.getBytes("UTF-8");
        } catch(UnsupportedEncodingException e) {
            if(log.isInfoEnabled()) {
                e.printStackTrace();
            } else {
                log.error("Making JWT Key Error ::: {}", e.getMessage());
            }
        }

        return key;
    }

    @Override
    public Map<String, Object> get(String key) {
        return null;
    }

    @Override
    public String getUserId() {
        return null;
    }

    // 전달 받은 token 의 유효성 체크
    @Override
    public boolean checkToken(String jwt) {
        try {
            // json web token 의 signature 를 근거로 토근 유효성을 검증한다.
            final Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(this.generateKey()).build().parseClaimsJws(jwt);
            log.debug("claims: {}", claims);
            return true;
        } catch(Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }
}
