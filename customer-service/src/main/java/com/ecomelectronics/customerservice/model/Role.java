package com.ecomelectronics.customerservice.model; // Sửa package cho đúng của bạn

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name; // Ví dụ: "ROLE_ADMIN", "ROLE_USER"
}
