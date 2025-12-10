package com.ecomelectronics.customerservice.controller;

import com.ecomelectronics.customerservice.dto.CreateUserRequest;
import com.ecomelectronics.customerservice.dto.LoginRequest;
import com.ecomelectronics.customerservice.dto.UserDto;
import com.ecomelectronics.customerservice.model.UserAccount;
import com.ecomelectronics.customerservice.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    private final UserAccountRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserAccountRepository userRepo,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    // ============= ĐĂNG KÝ =============
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody CreateUserRequest request) {
        // check trùng email
        if (userRepo.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email đã tồn tại");
        }

        UserAccount user = new UserAccount();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole()); // nếu có

        user = userRepo.save(user);

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFullName(user.getFullName());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole());
        // KHÔNG trả token, chỉ trả thông tin user
        return ResponseEntity.ok(userDto);
    }

    // ============= ĐĂNG NHẬP =============
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        // bước 1: nhờ Spring Security kiểm tra email + password
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()
                );

        authenticationManager.authenticate(authToken); // sai sẽ ném Exception

        // bước 2: lấy user từ DB và trả về cho frontend
        UserAccount user = userRepo.findByEmail(request.getEmail())
                .orElseThrow();

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFullName(user.getFullName());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole());

        return ResponseEntity.ok(userDto);
    }
}
