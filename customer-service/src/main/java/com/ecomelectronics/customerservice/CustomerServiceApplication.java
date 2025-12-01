package com.ecomelectronics.customerservice;

import com.ecomelectronics.customerservice.model.UserAccount;
import com.ecomelectronics.customerservice.model.UserRole;
import com.ecomelectronics.customerservice.repository.UserAccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class CustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }

    // Tạo admin mặc định nếu chưa có
    @Bean
    public CommandLineRunner initAdmin(UserAccountRepository userRepo,
                                       PasswordEncoder passwordEncoder) {
        return args -> {
            String adminEmail = "admin@shop.com";
            if (userRepo.findByEmail(adminEmail).isEmpty()) {
                UserAccount admin = UserAccount.builder()
                        .fullName("Default Admin")
                        .email(adminEmail)
                        .password(passwordEncoder.encode("admin123"))
                        .role(UserRole.ADMIN)
                        .active(true)
                        .build();
                userRepo.save(admin);
                System.out.println("Created default admin: " + adminEmail + " / admin123");
            }
        };
    }
}
