package ma.xproce.getrich.service;

import ma.xproce.getrich.dao.entities.RevokedToken;
import ma.xproce.getrich.dao.repositories.RevokedTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class RevokedTokenService implements RevokedTokenManager {

    @Autowired
    RevokedTokenRepository revokedTokenRepository;


    @Override
    public boolean isRevoked(String token) {
        return revokedTokenRepository.existsByToken(token);
    }

    @Override
    public RevokedToken addRevokedToken(RevokedToken revokedToken) {
        return revokedTokenRepository.save(revokedToken);
    }


}
