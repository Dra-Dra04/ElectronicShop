package com.ecomelectronics.adminservice.service;

import com.ecomelectronics.adminservice.dto.CreateUserRequest;
import com.ecomelectronics.adminservice.dto.UpdateUserRoleRequest;
import com.ecomelectronics.adminservice.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    private final RestTemplate restTemplate;
    private final String customerBaseUrl; // http://localhost:8082

    public AdminUserServiceImpl(RestTemplate restTemplate,
                                @Value("${services.customer-service.base-url}") String customerBaseUrl) {
        this.restTemplate = restTemplate;
        this.customerBaseUrl = customerBaseUrl;
    }

    @Override
    public List<UserDto> getAllUsers() {
        String url = customerBaseUrl + "/api/users";
        ResponseEntity<UserDto[]> res = restTemplate.getForEntity(url, UserDto[].class);
        UserDto[] body = res.getBody();
        return body == null ? Collections.emptyList() : Arrays.asList(body);
    }

    @Override
    public UserDto createUser(CreateUserRequest req) {
        // Nếu role null -> tạo customer
        String role = (req.getRole() == null || req.getRole().isBlank()) ? "CUSTOMER" : req.getRole().toUpperCase();

        String url = role.equals("ADMIN")
                ? customerBaseUrl + "/api/users/admin"
                : customerBaseUrl + "/api/users/customer";

        return restTemplate.postForObject(url, req, UserDto.class);
    }

    @Override
    public UserDto updateRole(Long id, UpdateUserRoleRequest req) {
        String url = customerBaseUrl + "/api/users/" + id + "/role";
        // PUT không trả body => dùng exchange nếu bạn muốn, còn đơn giản: gọi PUT rồi GET lại
        restTemplate.put(url, req);
        return restTemplate.getForObject(customerBaseUrl + "/api/users/" + id, UserDto.class);
    }

    @Override
    public void deleteUser(Long id) {
        restTemplate.delete(customerBaseUrl + "/api/users/" + id);
    }
    @Override
    public UserDto getUserById(Long id) {
        String url = customerBaseUrl + "/api/users/" + id;
        return restTemplate.getForObject(url, UserDto.class);
    }
}
