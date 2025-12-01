package com.ecomelectronics.customerservice.dto;

import com.ecomelectronics.customerservice.model.UserRole;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDto {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private UserRole role;
    private Boolean active;
    private LocalDateTime createdAt;
}
