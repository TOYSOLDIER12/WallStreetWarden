package ma.xproce.getrich.service;

import ma.xproce.getrich.dao.entities.RevokedToken;

public interface RevokedTokenManager {
    public boolean isRevoked(String token);
    public RevokedToken addRevokedToken(RevokedToken revokedToken);
}
