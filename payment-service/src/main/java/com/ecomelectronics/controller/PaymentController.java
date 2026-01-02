package com.ecomelectronics.controller;

import com.ecomelectronics.config.Config;
import com.ecomelectronics.dto.CreatePaymentRequest;
import com.ecomelectronics.dto.CreatePaymentResponse;
import com.ecomelectronics.dto.PaymentRestDTO;
import com.ecomelectronics.dto.TransactionStatusDTO;
import com.ecomelectronics.model.Payment;
import com.ecomelectronics.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {
    
    private final PaymentService paymentService;
    private final String frontendUrl;
    
    public PaymentController(
            PaymentService paymentService,
            @Value("${frontend.service.url}") String frontendUrl) {
        this.paymentService = paymentService;
        this.frontendUrl = frontendUrl;
    }
    @GetMapping("/create_payment")
    public ResponseEntity<?> createPayment() throws UnsupportedEncodingException {


        long amount = 10000000;
        String vnp_TxnRef = Config.getRandomNumber(8);
        String vnp_TmnCode = Config.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", Config.vnp_Version);
        vnp_Params.put("vnp_Command", Config.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl",Config.vnp_ReturnUrl);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 5);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = Config.vnp_PayUrl + "?" + queryUrl;

        PaymentRestDTO paymentRestDTO = new PaymentRestDTO();
        paymentRestDTO.setStatus("OK");
        paymentRestDTO.setMessage("Success");
        paymentRestDTO.setURL(paymentUrl);

        return ResponseEntity.status(HttpStatus.OK).body(paymentRestDTO);
    }

    @GetMapping("/payment_infor")
    public ResponseEntity<?> transaction(
            @RequestParam(value = "vpn_Amount") String amount,
            @RequestParam(value = "vpn_BankCode") String bankCode,
            @RequestParam(value = "vpn_OrderInfo") String orderInfo,
            @RequestParam(value = "vpn_ResponseCode") String responseCode
    ) {
        TransactionStatusDTO transactionStatusDTO = new TransactionStatusDTO();
        if(responseCode.equals("00")){
            transactionStatusDTO.setStatus("OK");
            transactionStatusDTO.setMessage("Success");
            transactionStatusDTO.setData("");
        }else{
            transactionStatusDTO.setStatus("FALSE");
            transactionStatusDTO.setMessage("Fail");
            transactionStatusDTO.setData("");
        }
        return ResponseEntity.status(HttpStatus.OK).body(transactionStatusDTO);
    }
    
    // ========== NEW ENDPOINTS FOR ORDER-PAYMENT INTEGRATION ==========
    
    /**
     * Tạo payment từ Frontend hoặc Order Service
     * Endpoint này để tương thích với frontend đang gọi POST /api/payments
     */
    @PostMapping
    public ResponseEntity<?> createPaymentFromFrontend(@Valid @RequestBody CreatePaymentRequest request) {
        try {
            CreatePaymentResponse response = paymentService.createPayment(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Tạo payment từ Order Service
     */
    @PostMapping("/create")
    public ResponseEntity<?> createPayment(@Valid @RequestBody CreatePaymentRequest request) {
        try {
            CreatePaymentResponse response = paymentService.createPayment(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Xử lý khi payment thành công
     */
    @PostMapping("/{paymentId}/success")
    public ResponseEntity<?> paymentSuccess(@PathVariable Long paymentId) {
        try {
            Payment payment = paymentService.processPaymentSuccess(paymentId);
            return ResponseEntity.ok(Map.of(
                    "paymentId", payment.getId(),
                    "status", payment.getStatus().name(),
                    "orderId", payment.getOrderId(),
                    "message", "Payment processed successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Xử lý khi payment thất bại
     */
    @PostMapping("/{paymentId}/failure")
    public ResponseEntity<?> paymentFailure(@PathVariable Long paymentId) {
        try {
            Payment payment = paymentService.processPaymentFailure(paymentId);
            return ResponseEntity.ok(Map.of(
                    "paymentId", payment.getId(),
                    "status", payment.getStatus().name(),
                    "message", "Payment failed"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Lấy thông tin payment theo ID
     */
    @GetMapping("/{paymentId}")
    public ResponseEntity<?> getPayment(@PathVariable Long paymentId) {
        try {
            Payment payment = paymentService.getPaymentById(paymentId);
            return ResponseEntity.ok(Map.of(
                    "id", payment.getId(),
                    "orderId", payment.getOrderId(),
                    "amount", payment.getAmount(),
                    "method", payment.getMethod().name(),
                    "status", payment.getStatus().name(),
                    "createdAt", payment.getCreatedAt(),
                    "updatedAt", payment.getUpdatedAt()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Lấy payment theo orderId
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getPaymentByOrderId(@PathVariable Long orderId) {
        try {
            Payment payment = paymentService.getPaymentByOrderId(orderId);
            return ResponseEntity.ok(Map.of(
                    "id", payment.getId(),
                    "orderId", payment.getOrderId(),
                    "amount", payment.getAmount(),
                    "method", payment.getMethod().name(),
                    "status", payment.getStatus().name(),
                    "createdAt", payment.getCreatedAt(),
                    "updatedAt", payment.getUpdatedAt()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Tạo VNPay payment URL và redirect đến VNPay gateway
     */
    @GetMapping("/vnpay/create")
    public ResponseEntity<?> createVNPayPayment(
            @RequestParam(value = "paymentId") Long paymentId) throws UnsupportedEncodingException {
        try {
            // Lấy thông tin payment
            Payment payment = paymentService.getPaymentById(paymentId);
            
            if (payment == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Payment not found"));
            }
            
            // Tạo VNPay payment URL
            long amount = payment.getAmount().longValue() * 100; // VNPay yêu cầu amount tính bằng xu
            String vnp_TxnRef = Config.getRandomNumber(8);
            String vnp_TmnCode = Config.vnp_TmnCode;
            
            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", Config.vnp_Version);
            vnp_Params.put("vnp_Command", Config.vnp_Command);
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(amount));
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + payment.getOrderId());
            vnp_Params.put("vnp_OrderType", "other");
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_ReturnUrl", Config.vnp_ReturnUrl + "?paymentId=" + paymentId);
            vnp_Params.put("vnp_IpAddr", "127.0.0.1");
            
            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnp_CreateDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
            
            cld.add(Calendar.MINUTE, 15);
            String vnp_ExpireDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
            
            // Sắp xếp và tạo hash
            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            Iterator<String> itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = itr.next();
                String fieldValue = vnp_Params.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }
            
            String queryUrl = query.toString();
            String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hashData.toString());
            queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
            String paymentUrl = Config.vnp_PayUrl + "?" + queryUrl;
            
            // Redirect đến VNPay
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", paymentUrl)
                    .body(Map.of(
                            "redirectUrl", paymentUrl,
                            "message", "Redirecting to VNPay..."
                    ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Callback từ VNPay sau khi thanh toán
     * VNPay sẽ redirect về đây với các tham số từ query string
     */
    @GetMapping("/vnpay-return")
    public ResponseEntity<?> vnpayReturn(
            @RequestParam(value = "vnp_ResponseCode", required = false) String responseCode,
            @RequestParam(value = "vnp_TxnRef", required = false) String txnRef,
            @RequestParam(value = "paymentId", required = false) Long paymentId) {
        try {
            if (paymentId == null) {
                // Nếu không có paymentId, redirect về frontend với lỗi
                String errorUrl = frontendUrl + "/orderhistory.html?payment=error&message=Payment ID not found";
                return ResponseEntity.status(HttpStatus.FOUND)
                        .header("Location", errorUrl)
                        .build();
            }
            
            String redirectUrl;
            if ("00".equals(responseCode)) {
                // Thanh toán thành công
                Payment payment = paymentService.processPaymentSuccess(paymentId);
                redirectUrl = frontendUrl + "/orderhistory.html?payment=success&orderId=" + payment.getOrderId();
            } else {
                // Thanh toán thất bại
                Payment payment = paymentService.processPaymentFailure(paymentId);
                redirectUrl = frontendUrl + "/orderhistory.html?payment=failed&orderId=" + payment.getOrderId();
            }
            
            // Redirect về frontend
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", redirectUrl)
                    .build();
        } catch (Exception e) {
            // Nếu có lỗi, redirect về frontend với thông báo lỗi
            String errorUrl = frontendUrl + "/orderhistory.html?payment=error&message=" + 
                    URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", errorUrl)
                    .build();
        }
    }
    
    /**
     * Callback từ VNPay sau khi thanh toán (endpoint cũ, giữ lại để tương thích)
     */
    @GetMapping("/vnpay/callback")
    public ResponseEntity<?> vnpayCallback(
            @RequestParam(value = "vnp_ResponseCode", required = false) String responseCode,
            @RequestParam(value = "vnp_TxnRef", required = false) String txnRef,
            @RequestParam(value = "paymentId", required = false) Long paymentId) {
        // Redirect đến vnpay-return
        return vnpayReturn(responseCode, txnRef, paymentId);
    }
}
