package com.github.guninigor75.social_media.web.security;

import com.github.guninigor75.social_media.domain.exception.AccessDeniedException;
import com.github.guninigor75.social_media.domain.user.Role;
import com.github.guninigor75.social_media.domain.user.User;
import com.github.guninigor75.social_media.service.UserService;
import com.github.guninigor75.social_media.service.props.JwtProperties;
import com.github.guninigor75.social_media.web.dto.auth.JwtResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    private final UserService userService;
    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    public String createAccessToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.put("id", user.getId());
        claims.put("roles", getRolesFromUser(user.getRoles()));
        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + jwtProperties.getAccess().toMillis());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(key)
                .compact();

    }

    public String createRefreshToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.put("id", user.getId());
        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + jwtProperties.getRefresh().toMillis());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(key)
                .compact();

    }

    public JwtResponse updateUserTokens(String refreshToken) {
        if (!validatedToken(refreshToken)) {
            throw new AccessDeniedException();
        }
        JwtResponse jwtResponse = new JwtResponse();
        Long userId = Long.valueOf(getUserId(refreshToken));
        User user = userService.getById(userId);
        jwtResponse.setId(userId);
        jwtResponse.setUsername(user.getUsername());
        jwtResponse.setAccessToken(createAccessToken(user));
        jwtResponse.setRefreshToken(createRefreshToken(user));
        return jwtResponse;
    }

    public boolean validatedToken(String token) {
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
        Date expiration = claims.getBody().getExpiration();
        return expiration.after(new Date());
    }

    public String getUserName(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("username")
                .toString();
    }

    public List<String> getRolesFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("roles", List.class);
    }

    private String getUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("id")
                .toString();
    }

    private List<String> getRolesFromUser(Collection<Role> roles) {
        return roles.stream()
                .map(Role::getName)
                .toList();
    }
}
