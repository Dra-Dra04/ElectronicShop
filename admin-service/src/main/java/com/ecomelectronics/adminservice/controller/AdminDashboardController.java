package com.ecomelectronics.adminservice.controller;

import com.ecomelectronics.adminservice.dto.DashboardStatsDto;
import com.ecomelectronics.adminservice.service.AdminDashboardService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/dashboard")
@CrossOrigin(origins = "*")
public class AdminDashboardController {

    private final AdminDashboardService dashboardService;

    public AdminDashboardController(AdminDashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/overview")
    public DashboardStatsDto getOverview() {
        return dashboardService.getOverview();
    }
}
