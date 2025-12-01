package com.ecomelectronics.customerservice.dto;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String fullName;
    private String phone;
    private String address;
    private Boolean active;
}
