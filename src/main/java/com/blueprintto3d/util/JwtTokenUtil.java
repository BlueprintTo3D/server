package com.blueprintto3d.util;

import com.blueprintto3d.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class JwtTokenUtil {

    private static Claims extractClaims(String token, String key){
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }

    public static String isValid(String token, String key){
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
            return "OK";
        } catch (ExpiredJwtException e) {
            return ErrorCode.EXPIRE_TOKEN.name();
        } catch (Exception e) {
            return ErrorCode.INVALID_TOKEN.name();
        }
    }

    public static Long getUserNo(String token, String key) {
        return Long.valueOf(extractClaims(token, key).get("userNo").toString());
    }

    public static String createToken(Long userNo, String key, long expireTimeMs) {
        Claims claims = Jwts.claims();
        claims.put("userNo", userNo);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }
}
