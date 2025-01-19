package ma.xproce.getrich.web;

import jakarta.validation.Valid;
import ma.xproce.getrich.config.AuthenticationResponse;
import ma.xproce.getrich.config.MyUserPrincipal;
import ma.xproce.getrich.dao.entities.Member;
import ma.xproce.getrich.dao.entities.RevokedToken;
import ma.xproce.getrich.service.AuthenticationService;
import ma.xproce.getrich.service.JwtService;
import ma.xproce.getrich.service.MemberManager;
import ma.xproce.getrich.service.RevokedTokenManager;
import ma.xproce.getrich.service.dto.LoginResponse;
import ma.xproce.getrich.service.dto.MemberDto;
import ma.xproce.getrich.service.dto.MemberDtoADD;
import ma.xproce.getrich.service.mappers.MemberMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.UUID;


@RestController
public class MemberController {

    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);


    @Value("${upload-dir}")
    private String uploadDir;

    @Autowired
    JwtService jwtService;
    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    MemberManager memberManager;
    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private RevokedTokenManager revokedTokenManager;


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody MemberDtoADD loginUserDto) {


        AuthenticationResponse authenticatedUser = authenticationService.authenticate(loginUserDto);

        if (!authenticatedUser.isSuccess()) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse("Authentication failed", 0)); // You can also provide a meaningful response
        }

        String jwtToken = jwtService.generateToken(new MyUserPrincipal(authenticatedUser.getMember(), null));

        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }





    @PostMapping("/signup")
    public ResponseEntity<Member> register(@RequestPart("user") @Valid MemberDtoADD registerUserDto,
                                           @RequestPart(value = "profilePicture", required = false) MultipartFile profile) {
        if (profile != null) {
            System.out.println("Received profile picture: " + profile.getOriginalFilename());
            try {
                byte[] bytes = profile.getBytes();
                String originalFilename = profile.getOriginalFilename();
                String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String uniqueFilename = UUID.randomUUID().toString() + extension;
                Path uploadPath = Paths.get(uploadDir, uniqueFilename);
                Files.write(uploadPath, bytes);
                registerUserDto.setProfile("/" + uniqueFilename);
            } catch (IOException e) {

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }
        else {

            System.out.println("No profile picture uploaded");
        }
        registerUserDto.setToken(UUID.randomUUID());
        Member registeredUser = authenticationService.signup(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }



    @PostMapping("/sign-out")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {

        logger.debug("Received logout request with token: {}", token);

        try {
        // Extract the token without "Bearer " prefix
        String jwtToken = token.replace("Bearer ", "");

        // Add the token to the revoked list
        RevokedToken revokedToken = new RevokedToken();
        revokedToken.setToken(jwtToken);
        revokedTokenManager.addRevokedToken(revokedToken);

        return ResponseEntity.ok("Logged out successfully.");
    } catch (Exception e) {
            // Log the error and return an appropriate response
            logger.error("Error revoking token: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error logging out.");
        }

    }

    @GetMapping("/user/profile")
    public ResponseEntity<MemberDto> getUserProfile(Principal principal) {

        String username = principal.getName();



        Member member = memberManager.findByUsername(username);
        MemberDto memberDto = memberMapper.MemberToMemberDTO(member);


        return ResponseEntity.ok(memberDto);
    }


}
