package ma.xproce.getrich.web;

import ma.xproce.getrich.config.MyUserDetailsService;
import ma.xproce.getrich.dao.entities.Enterprise;
import ma.xproce.getrich.service.EnterpriseManager;
import ma.xproce.getrich.service.JwtService;
import ma.xproce.getrich.service.MemberManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EnterpriseController {
    @Autowired
    EnterpriseManager enterpriseManager;
    @Autowired
    JwtService jwtService;
    @Autowired
    MemberManager memberManager;
    @Autowired
    MyUserDetailsService myUserDetailsService;

    @GetMapping("/enterprises")
    public ResponseEntity<List<Enterprise>> getEnterprises(@RequestHeader("Authorization") String token) {
        try {
            String username = jwtService.extractUsername(token);


            if (username == null ||!jwtService.isTokenValid(token,  myUserDetailsService.loadUserByUsername(username))) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
            List<Enterprise> enterprises = enterpriseManager.getAllEnterprises();
            return ResponseEntity.ok(enterprises);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
