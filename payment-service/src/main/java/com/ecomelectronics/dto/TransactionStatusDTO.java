package com.ecomelectronics.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class TransactionStatusDTO implements Serializable {
    private String status;
    private String message;
    private String data;
}
