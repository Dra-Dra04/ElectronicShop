package com.ecomelectronics.dto;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMomoRequest {
    private String partnerCode;
    private String requestType;
    private String ipnUrl;
    private String orderId;
    private Long amount;
    private String orderInfo;
    private String redirectUrl;
    private String lang;
    private String extraData;
    private String requestId;
    private String signature;
    private String accessKey;
    private List<Object> items;
}
