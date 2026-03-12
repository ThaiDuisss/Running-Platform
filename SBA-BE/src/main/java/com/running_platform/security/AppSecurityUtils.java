package com.example.oauth2.security;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class AppSecurityUtils {

    public static String ROLE_DEFAULT = "ROLE_DEFAULT";
    /**
     * Converts list of roles into Collection of GrantedAuthority
     *
     * @param roles
     * @return Collection<? extends GrantedAuthority>
     */
    public static Collection<? extends GrantedAuthority> convertRolesSetToGrantedAuthorityList(Set<String> roles) {
        Collection<GrantedAuthority> authorities = new HashSet<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }

    public static Set<String> convertGrantedAuthorityListToRolesSet(Collection<? extends GrantedAuthority> authorities) {
        return AuthorityUtils.authorityListToSet(authorities);
    }

    public static Authentication getAuthenticationObject() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static CustomUserDetails getCurrentUserPrinciple() {
        Authentication authentication = getAuthenticationObject();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof CustomUserDetails) {
                return ((CustomUserDetails) principal);
            }
        }
        return null;
    }

    public static Optional<Long> getCurrentUserId() {
        Optional<Long> optionalUserId = Optional.ofNullable(getCurrentUserPrinciple())
                .map(customUserDetails -> customUserDetails.getUserEntity())
                .map(userEntity -> userEntity.getId());
        return optionalUserId;
    }

    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
    }
}
