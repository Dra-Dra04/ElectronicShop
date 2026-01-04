package com.ecomelectronics.adminservice.controller;

import com.ecomelectronics.adminservice.dto.DashboardOverviewDto;
import com.ecomelectronics.adminservice.service.AdminDashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminDashboardController {

    private final AdminDashboardService dashboardService;

    // Tiêm Service vào Controller
    public AdminDashboardController(AdminDashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    // API này sẽ gọi hàm getOverview() bên Service
    @GetMapping("/dashboard/overview")
    public ResponseEntity<DashboardOverviewDto> getOverview() {
        return ResponseEntity.ok(dashboardService.getOverview());
    }
}