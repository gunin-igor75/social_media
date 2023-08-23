package com.github.guninigor75.social_media.security.security_expression;

import com.github.guninigor75.social_media.security.SecurityUser;
import com.github.guninigor75.social_media.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service("customSecurityExpression")
@RequiredArgsConstructor
public class CustomSecurityExpression {

    private final PostService postService;

    public boolean canAccessPost(Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        return postService.isOwnerPost(postId, user.getId()) || hasAnyRole(authentication, "ROLE_ADMIN");
    }

    private boolean hasAnyRole(Authentication authentication, String... roles) {
        for (String role : roles) {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
            if (authentication.getAuthorities().contains(authority)) {
                return true;
            }
        }
        return false;
    }
}
