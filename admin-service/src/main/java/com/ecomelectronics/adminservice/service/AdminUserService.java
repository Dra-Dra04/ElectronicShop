package com.ecomelectronics.adminservice.service;

import com.ecomelectronics.adminservice.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class AdminUserService {

    private final RestTemplate restTemplate;
    private final String customerBaseUrl;

    public AdminUserService(
            RestTemplate restTemplate,
            @Value("${services.customer-service.base-url}") String customerBaseUrl
    ) {
        this.restTemplate = restTemplate;
        this.customerBaseUrl = customerBaseUrl;
    }

    // Lấy hết user, có thể lọc theo role (ADMIN/CUSTOMER/SALER)
    public List<UserDto> getAllUsers(String role) {
        String url = customerBaseUrl + "/api/users";
        if (role != null && !role.isBlank()) {
            url += "?role=" + role;
        }

        ResponseEntity<UserDto[]> response =
                restTemplate.getForEntity(url, UserDto[].class);

        UserDto[] body = response.getBody();
        return body == null ? List.of() : Arrays.asList(body);
    }

    public UserDto getUserById(Long id) {
        String url = customerBaseUrl + "/api/users/" + id;
        return restTemplate.getForObject(url, UserDto.class);
    }

    public void deleteUser(Long id) {
        String url = customerBaseUrl + "/api/users/" + id;
        restTemplate.delete(url);
    }
}
