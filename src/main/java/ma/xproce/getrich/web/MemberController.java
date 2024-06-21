package ma.xproce.getrich.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import ma.xproce.getrich.dao.entities.Member;
import ma.xproce.getrich.service.AuthenticationService;
import ma.xproce.getrich.service.JwtService;
import ma.xproce.getrich.service.MemberManager;
import ma.xproce.getrich.service.dto.LoginResponse;
import ma.xproce.getrich.service.dto.MemberDto;
import ma.xproce.getrich.service.dto.MemberDtoADD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


@RestController
public class MemberController {


    @Value("${upload-dir}")
    private String uploadDir;

    @Autowired
    JwtService jwtService;
    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    MemberManager memberManager;




    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody MemberDto loginUserDto) {
        Member authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken((UserDetails) authenticatedUser);

        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }





    @PostMapping("/signup")
    public ResponseEntity<Member> register(@RequestPart("user") @Valid MemberDtoADD registerUserDto,
                                           @RequestPart(value = "profilePicture", required = false) MultipartFile profile) {
        if (profile != null) {
            try {
                byte[] bytes = profile.getBytes();
                String originalFilename = profile.getOriginalFilename();
                String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String uniqueFilename = UUID.randomUUID().toString() + extension;
                Path uploadPath = Paths.get(uploadDir, uniqueFilename);
                Files.write(uploadPath, bytes);
                registerUserDto.setProfile("/" + uniqueFilename);
            } catch (IOException e) {
                // Handle the exception properly
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }
        registerUserDto.setToken(UUID.randomUUID());
        Member registeredUser = authenticationService.signup(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }



    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        return "redirect:login";
    }

}
