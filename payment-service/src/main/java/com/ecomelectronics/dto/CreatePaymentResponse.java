package com.ecomelectronics.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePaymentResponse {
    private Long paymentId;
    private String status;
    private String payUrl;
}
