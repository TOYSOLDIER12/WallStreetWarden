package ma.xproce.getrich.config;

import ma.xproce.getrich.dao.entities.Member;
import ma.xproce.getrich.service.MemberManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService {
    private final MemberManager memberManager;

    @Autowired
    public MyUserDetailsService(MemberManager memberManager) {
        this.memberManager = memberManager;
    }



    public MyUserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberManager.findByUsername(username);

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(member.getRole()));
        return new MyUserPrincipal(member, authorities);

    }
    private List<GrantedAuthority> getAuthorities(List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }

}
