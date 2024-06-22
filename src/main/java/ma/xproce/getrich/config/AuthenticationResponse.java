package ma.xproce.getrich.config;

import ma.xproce.getrich.dao.entities.Member;

public class AuthenticationResponse {
    private String token;
    private Long expirationTime;
    private Member member;

    public AuthenticationResponse(String token, Long expirationTime, Member member) {
        this.token = token;
        this.expirationTime = expirationTime;
        this.member = member;
    }

    public String getToken() {
        return token;
    }

    public Long getExpirationTime() {
        return expirationTime;
    }

    public Member getMember() {
        return member;
    }
}
