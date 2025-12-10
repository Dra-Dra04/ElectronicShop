package com.ecomelectronics.customerservice.dto;

public class LoginRequest {
    private String email;     // hoặc username tùy bạn lưu trong UserAccount
    private String password;

    public LoginRequest() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
