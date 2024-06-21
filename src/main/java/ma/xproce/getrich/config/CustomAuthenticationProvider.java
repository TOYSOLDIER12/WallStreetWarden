package ma.xproce.getrich.config;

import ma.xproce.getrich.dao.entities.Member;
import ma.xproce.getrich.service.AuthenticationService;
import ma.xproce.getrich.service.dto.MemberDto;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.List;

public class CustomAuthenticationProvider implements AuthenticationProvider {


    AuthenticationService authenticationService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        Member member = authenticationService.authenticate(new MemberDto(username, password));
        if (member != null) {
            return new UsernamePasswordAuthenticationToken(member.getUsername(), member.getPassword());
        } else {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }

    private List<GrantedAuthority> getAuthorities(Member member) {
        // implement authority mapping logic here
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
    }
}
