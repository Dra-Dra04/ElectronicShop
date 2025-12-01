package com.ecomelectronics.customerservice.dto;

import com.ecomelectronics.customerservice.model.UserRole;
import lombok.Data;

@Data
public class CreateUserRequest {
    private String fullName;
    private String email;
    private String password;
    private String phone;
    private String address;
    private UserRole role;   // nếu null sẽ default CUSTOMER
}
