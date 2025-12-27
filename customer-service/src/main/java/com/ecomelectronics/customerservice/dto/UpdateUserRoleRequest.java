package com.ecomelectronics.customerservice.dto;

import com.ecomelectronics.customerservice.model.UserRole;
import lombok.Data;

@Data
public class UpdateUserRoleRequest {
    private UserRole role; // ADMIN / CUSTOMER / SALER
}
