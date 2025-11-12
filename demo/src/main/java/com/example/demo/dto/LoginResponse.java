package com.example.demo.dto;

public class LoginResponse {
    private Integer idUser;
    private String name;
    private String email;
    private String token;

    public LoginResponse() {}

    public LoginResponse(Integer idUser, String name, String email, String token) {
        this.idUser = idUser;
        this.name = name;
        this.email = email;
        this.token = token;
    }

    // getters y setters
    public Integer getIdUser() { return idUser; }
    public void setIdUser(Integer idUser) { this.idUser = idUser; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}