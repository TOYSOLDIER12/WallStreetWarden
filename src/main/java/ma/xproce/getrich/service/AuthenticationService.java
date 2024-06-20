package ma.xproce.getrich.service;

import ma.xproce.getrich.dao.entities.Member;
import ma.xproce.getrich.service.dto.MemberDto;
import ma.xproce.getrich.service.dto.MemberDtoADD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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



    public Member signup(MemberDtoADD input) {
        Member user = new Member();
                user.setName(input.getName());
                user.setUsername(input.getUsername());
                user.setPassword(passwordEncoder.encode(input.getPassword()));

        return userRepository.addMember(user);
    }

    public Member authenticate(MemberDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getUsername(),
                        input.getPassword()
                )
        );

        return userRepository.findByUsername(input.getUsername())
                .orElseThrow();
    }
}
