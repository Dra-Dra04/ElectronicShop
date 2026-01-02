package com.ecomelectronics.adminservice.dto;

import lombok.Data;

@Data
public class CreateUserRequest {
    private String fullName;
    private String email;
    private String password;
    private String phone;
    private String address;
    private String role; // nếu null -> CUSTOMER
}
