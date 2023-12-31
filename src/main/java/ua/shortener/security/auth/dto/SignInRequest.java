package ua.shortener.security.auth.dto;

import lombok.Data;

@Data
public class SignInRequest {
    private String email;
    private String password;

    public SignInRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
