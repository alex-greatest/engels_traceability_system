package com.rena.application.config.security;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import jakarta.annotation.Nonnull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;

@BrowserCallable
public class UserInfoService {

    @AnonymousAllowed
    @Nonnull
    public UserInfo getUserInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal() instanceof UserSecurity userSecurity) {
            final List<String> authorities = userSecurity.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();
            return new UserInfo(auth.getName(), userSecurity.getCode(), authorities);
        }
        final List<String> authorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return new UserInfo(auth.getName(), -1, authorities);
    }
}
