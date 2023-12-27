package ua.shortener.security.auth.dto;

import lombok.Data;

@Data
public class SignUpRequest {
    private String name;
    private String password;
    private String email;

    public SignUpRequest(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }
}
