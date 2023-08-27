package com.github.guninigor75.social_media.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.security.Key;
import java.util.Collections;
import java.util.Date;

public class TokenAuthenticationService {

    static final long EXPIRATION_TIME = 864_000_000; // 10 days
    static final String SECRET = "ThisIsASecret";
    static final String TOKEN_PREFIX = "Bearer";
    static final String HEADER_STRING = "Authorization";

    public static void addAuthentication(HttpServletResponse res, String username) {

        String jwt = createToken(username);

        res.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + jwt);
    }

    public static Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            // parse the token.
            String user = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody()
                    .getSubject();

            return user != null ?
                    new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList()) :
                    null;
        }
        return null;
    }

    public static String createToken(String username) {
        Key key = Keys.hmacShaKeyFor(SECRET.getBytes());
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("id", 1L);
//        claims.put("roles", getRolesFromUser(user.getRoles()));
        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + EXPIRATION_TIME);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(key)
                .compact();

    }
}
