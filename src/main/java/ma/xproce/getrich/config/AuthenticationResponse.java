package ma.xproce.getrich.config;

import ma.xproce.getrich.dao.entities.Member;

public class AuthenticationResponse {
    private String token;
    private Long expirationTime;
    private Member member;
    private boolean success;
    private String message;

    public AuthenticationResponse(String token, Long expirationTime, Member member) {
        this.token = token;
        this.expirationTime = expirationTime;
        this.member = member;
        this.success = true;
    }

    public AuthenticationResponse() {

    }

    public static AuthenticationResponse error(String message) {
        AuthenticationResponse response = new AuthenticationResponse();
        response.success = false;
        response.message = message;
        return response;
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
    public String getMessage() {
        return message;
    }
    public boolean isSuccess() {
        return success;
    }
}
