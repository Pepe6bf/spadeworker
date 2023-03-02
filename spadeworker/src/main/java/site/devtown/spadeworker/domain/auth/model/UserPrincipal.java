package site.devtown.spadeworker.domain.auth.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import site.devtown.spadeworker.domain.user.model.constant.AuthProviderType;
import site.devtown.spadeworker.domain.user.model.constant.UserStatus;
import site.devtown.spadeworker.domain.user.model.entity.User;

import java.util.Collection;
import java.util.Map;

public record UserPrincipal(
        String personalId,
        AuthProviderType providerType,
        UserStatus userStatus,
        Collection<GrantedAuthority> authorities,
        Map<String, Object> oAuth2UserInfoAttributes
) implements UserDetails, OAuth2User, OidcUser {

    public static UserPrincipal from(
            User user,
            Collection<GrantedAuthority> authorities
    ) {
        return from(user, authorities, null);
    }

    public static UserPrincipal from(
            User user,
            Collection<GrantedAuthority> authorities,
            Map<String, Object> oAuth2UserInfo
    ) {
        return new UserPrincipal(
                user.getPersonalId(),
                user.getProviderType(),
                user.getStatus(),
                authorities,
                oAuth2UserInfo
        );
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2UserInfoAttributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
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
        return this.userStatus == UserStatus.ACTIVE;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.userStatus == UserStatus.ACTIVE;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.userStatus == UserStatus.ACTIVE;
    }

    @Override
    public boolean isEnabled() {
        return this.userStatus == UserStatus.ACTIVE;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName()).append(" [");
        sb.append("personalId=").append(this.personalId).append(", ");
        sb.append("providerType=").append(this.providerType).append(", ");
        sb.append("userStatus=").append(this.userStatus).append(", ");
        sb.append("Granted Authorities=").append(this.authorities).append("], ");
        sb.append("oAuth2UserInfoAttributes=[PROTECTED]");
        return sb.toString();
    }
}