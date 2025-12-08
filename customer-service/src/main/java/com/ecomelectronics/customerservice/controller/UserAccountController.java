package com.ecomelectronics.customerservice.controller;

import com.ecomelectronics.customerservice.dto.CreateUserRequest;
import com.ecomelectronics.customerservice.dto.UpdateUserRequest;
import com.ecomelectronics.customerservice.dto.UserDto;
import com.ecomelectronics.customerservice.model.UserAccount;
import com.ecomelectronics.customerservice.model.UserRole;
import com.ecomelectronics.customerservice.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserAccountController {

    @Autowired
    private UserAccountRepository userRepo;

    // ---- Helpers mapping ----
    private UserDto toDto(UserAccount u) {
        UserDto dto = new UserDto();
        dto.setId(u.getId());
        dto.setFullName(u.getFullName());
        dto.setEmail(u.getEmail());
        dto.setPhone(u.getPhone());
        dto.setAddress(u.getAddress());
        dto.setRole(u.getRole());
        dto.setActive(u.getActive());
        dto.setCreatedAt(u.getCreatedAt());
        return dto;
    }

    // ---- CRUD chung (user nào cũng được) ----

    // GET /api/users?role=ADMIN/CUSTOMER
    @GetMapping
    public List<UserDto> getAll(@RequestParam(required = false) UserRole role) {
        List<UserAccount> users;
        if (role != null) {
            users = userRepo.findByRole(role);
        } else {
            users = userRepo.findAll();
        }
        return users.stream().map(this::toDto).toList();
    }

    // GET /api/users/{id}
    @GetMapping("/{id}")
    public UserDto getById(@PathVariable Long id) {
        UserAccount user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return toDto(user);
    }

    // POST /api/users  (role có thể gửi từ client)
    @PostMapping
    public UserDto create(@RequestBody CreateUserRequest req) {
        UserRole role = (req.getRole() != null) ? req.getRole() : UserRole.CUSTOMER;

        String encodedPassword = passwordEncoder.encode(req.getPassword());

        UserAccount user = UserAccount.builder()
                .fullName(req.getFullName())
                .email(req.getEmail())
                .password(encodedPassword)
                .phone(req.getPhone())
                .address(req.getAddress())
                .role(role)
                .active(true)
                .build();

        user = userRepo.save(user);
        return toDto(user);
    }
    @Autowired
    private PasswordEncoder passwordEncoder;


    // PUT /api/users/{id}
    @PutMapping("/{id}")
    public UserDto update(@PathVariable Long id, @RequestBody UpdateUserRequest req) {
        UserAccount user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (req.getFullName() != null) user.setFullName(req.getFullName());
        if (req.getPhone() != null) user.setPhone(req.getPhone());
        if (req.getAddress() != null) user.setAddress(req.getAddress());
        if (req.getActive() != null) user.setActive(req.getActive());

        user = userRepo.save(user);
        return toDto(user);
    }

    // DELETE /api/users/{id}  (có thể coi là xóa hẳn)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        if (!userRepo.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepo.deleteById(id);
    }

    // ---- Endpoint riêng cho ADMIN / CUSTOMER (tiện dùng sau này) ----

    // POST /api/users/admin  -> tạo admin
    @PostMapping("/admin")
    public UserDto createAdmin(@RequestBody CreateUserRequest req) {
        req.setRole(UserRole.ADMIN);
        return create(req);
    }

    // POST /api/users/customer -> tạo customer
    @PostMapping("/customer")
    public UserDto createCustomer(@RequestBody CreateUserRequest req) {
        req.setRole(UserRole.CUSTOMER);
        return create(req);
    }

    // GET /api/users/admins
    @GetMapping("/admins")
    public List<UserDto> getAdmins() {
        return userRepo.findByRole(UserRole.ADMIN)
                .stream().map(this::toDto).toList();
    }

    // GET /api/users/customers
    @GetMapping("/customers")
    public List<UserDto> getCustomers() {
        return userRepo.findByRole(UserRole.CUSTOMER)
                .stream().map(this::toDto).toList();
    }



}
