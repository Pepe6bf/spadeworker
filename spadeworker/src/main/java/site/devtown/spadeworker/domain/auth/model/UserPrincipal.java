package site.devtown.spadeworker.domain.auth.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import site.devtown.spadeworker.domain.user.model.constant.AuthProviderType;
import site.devtown.spadeworker.domain.user.model.constant.UserRoleType;
import site.devtown.spadeworker.domain.user.model.constant.UserStatus;
import site.devtown.spadeworker.domain.user.model.entity.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public record UserPrincipal(
        String personalId,
        AuthProviderType providerType,
        UserStatus status,
        List<UserRoleType> roles,
        Map<String, Object> oAuth2UserInfoAttributes
) implements UserDetails, OAuth2User, OidcUser {
    public static UserPrincipal from(
            User user,
            List<UserRoleType> roles,
            Map<String, Object> oAuth2UserInfo
    ) {
        return new UserPrincipal(
                user.getPersonalId(),
                user.getProviderType(),
                user.getStatus(),
                roles,
                oAuth2UserInfo
        );
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2UserInfoAttributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(r -> new SimpleGrantedAuthority(r.toString()))
                .toList();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getName() {
        return this.personalId;
    }

    @Override
    public String getUsername() {
        return this.personalId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.status == UserStatus.ACTIVE;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.status == UserStatus.ACTIVE;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.status == UserStatus.ACTIVE;
    }

    @Override
    public boolean isEnabled() {
        return this.status == UserStatus.ACTIVE;
    }

    @Override
    public Map<String, Object> getClaims() {
        return null;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return null;
    }

    @Override
    public OidcIdToken getIdToken() {
        return null;
    }
}