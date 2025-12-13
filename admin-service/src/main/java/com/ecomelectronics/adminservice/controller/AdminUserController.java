package com.ecomelectronics.adminservice.controller;

import com.ecomelectronics.adminservice.dto.UserDto;
import com.ecomelectronics.adminservice.service.AdminUserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@CrossOrigin(origins = "*")
public class AdminUserController {

    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    // GET /api/admin/users  hoặc /api/admin/users?role=ADMIN
    @GetMapping
    public List<UserDto> getAll(@RequestParam(required = false) String role) {
        return adminUserService.getAllUsers(role);
    }

    // GET /api/admin/users/{id}
    @GetMapping("/{id}")
    public UserDto getById(@PathVariable Long id) {
        return adminUserService.getUserById(id);
    }

    // DELETE /api/admin/users/{id}
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        adminUserService.deleteUser(id);
    }
}
