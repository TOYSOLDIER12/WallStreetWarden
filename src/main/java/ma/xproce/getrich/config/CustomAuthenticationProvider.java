package ma.xproce.getrich.config;

import ma.xproce.getrich.dao.entities.Member;
import ma.xproce.getrich.service.JwtService;
import ma.xproce.getrich.service.MemberManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.List;

public class CustomAuthenticationProvider implements AuthenticationProvider {


    private final MemberManager memberManager;
    private final JwtService jwtService;

    public CustomAuthenticationProvider(MemberManager memberManager, JwtService jwtService) {
        this.memberManager = memberManager;
        this.jwtService = jwtService;
    }




    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        Member member = memberManager.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (password.equals(member.getPassword())) {
            // Authentication successful
            MyUserPrincipal userDetails = new MyUserPrincipal(member, null);

            // Generate JWT token
            String token = jwtService.generateToken(userDetails);

            return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
        } else {
            // Authentication failed
            throw new BadCredentialsException("Invalid username or password");
        }
    }
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private List<GrantedAuthority> getAuthorities(Member member) {
        // Implement authority mapping logic based on your application's requirements.
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }
}
