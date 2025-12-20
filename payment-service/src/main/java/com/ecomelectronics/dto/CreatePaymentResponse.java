package com.ecomelectronics.dto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreatePaymentResponse {
    private Long paymentId;
    private String status;
    private String payUrl;
}
