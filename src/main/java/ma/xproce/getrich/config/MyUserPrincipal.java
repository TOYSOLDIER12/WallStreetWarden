package ma.xproce.getrich.config;

import lombok.Getter;
import lombok.Setter;
import ma.xproce.getrich.dao.entities.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class MyUserPrincipal implements UserDetails {
    private Member member;
    private List<GrantedAuthority> authorities;


    public MyUserPrincipal(Member member, List<GrantedAuthority> authorities) {
        this.member = member;
        this.authorities = authorities;
    }
    @Override
    public Collection<GrantedAuthority> getAuthorities() {
         return authorities;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
         return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
