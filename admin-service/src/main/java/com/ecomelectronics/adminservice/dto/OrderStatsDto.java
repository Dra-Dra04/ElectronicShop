package com.ecomelectronics.adminservice.dto;

import java.math.BigDecimal;

public class OrderStatsDto {
    private Long count;
    private BigDecimal revenue;

    public OrderStatsDto() {
    }

    public OrderStatsDto(Long count, BigDecimal revenue) {
        this.count = count;
        this.revenue = revenue;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }
}
