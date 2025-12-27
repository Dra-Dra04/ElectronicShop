package com.ecomelectronics.adminservice.service;

import com.ecomelectronics.adminservice.dto.CreateUserRequest;
import com.ecomelectronics.adminservice.dto.UpdateUserRoleRequest;
import com.ecomelectronics.adminservice.dto.UserDto;

import java.util.List;

public interface AdminUserService {

    // Hàm lấy danh sách user
    List<UserDto> getAllUsers();

    // Hàm tạo user mới
    UserDto createUser(CreateUserRequest req);

    // Hàm cập nhật quyền (Chức năng mới  đang làm)
    UserDto updateRole(Long id, UpdateUserRoleRequest req);

    // Hàm xóa user
    void deleteUser(Long id);

    // [QUAN TRỌNG] Thêm hàm này để tương thích với code cũ
    // (Vì code cũ của bạn có hàm này, nếu thiếu Controller sẽ báo lỗi)
    UserDto getUserById(Long id);
}