package ma.xproce.getrich.service;

import ma.xproce.getrich.config.AuthenticationResponse;
import ma.xproce.getrich.config.MyUserDetailsService;
import ma.xproce.getrich.config.MyUserPrincipal;
import ma.xproce.getrich.dao.entities.Member;
import ma.xproce.getrich.service.dto.MemberDtoADD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    @Autowired
    MemberManager userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtService jwtService;
    @Autowired
    MyUserDetailsService myUserDetailsService;



    public Member signup(MemberDtoADD input) {
        Member user = new Member();
                user.setName(input.getName());
                user.setUsername(input.getUsername());
                user.setPassword(passwordEncoder.encode(input.getPassword()));
                user.setRole("user");
                user.setProfile(input.getProfile());

        return userRepository.addMember(user);
    }

    public AuthenticationResponse authenticate(MemberDtoADD input) {
        try {





            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            input.getUsername(),
                            input.getPassword()
                    )
            );

            // If we reach this point, authentication was successful, hopefully :/

            Member member = userRepository.findByUsername(input.getUsername());
            MyUserPrincipal userDetails = (MyUserPrincipal) authentication.getPrincipal();

            String token = jwtService.generateToken(userDetails);
            Long expirationTime = jwtService.getExpirationTime();

            return new AuthenticationResponse(token, expirationTime, member);
        } catch (AuthenticationException e) {
            // Handle the authentication exception
            // You can throw a custom exception or return an error response
            throw new RuntimeException("Invalid username or password", e);
        }

        catch (Exception e) {
            // Handle any other unexpected exceptions
            return AuthenticationResponse.error("An unexpected error occurred");
        }

    }
}
