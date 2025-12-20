package com.ecomelectronics.adminservice.service;

import com.ecomelectronics.adminservice.dto.OrderResponse;
import com.ecomelectronics.adminservice.dto.OrderSummaryResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class AdminOrderService {

    private final RestTemplate restTemplate;

    @Value("${order.service.url:http://localhost:8083}")
    private String orderServiceUrl;

    public AdminOrderService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<OrderSummaryResponse> list(String status) {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(orderServiceUrl + "/api/admin/orders");

        if (status != null && !status.isBlank()) {
            builder.queryParam("status", status);
        }

        String url = builder.toUriString();

        OrderSummaryResponse[] arr = restTemplate.getForObject(url, OrderSummaryResponse[].class);
        return arr == null ? List.of() : Arrays.asList(arr);
    }

    public OrderResponse detail(Long orderId) {
        return restTemplate.getForObject(orderServiceUrl + "/api/admin/orders/" + orderId, OrderResponse.class);
    }

    public OrderSummaryResponse approve(Long orderId) {
        return patch(orderServiceUrl + "/api/admin/orders/" + orderId + "/approve");
    }

    public OrderSummaryResponse ship(Long orderId) {
        return patch(orderServiceUrl + "/api/admin/orders/" + orderId + "/ship");
    }

    public OrderSummaryResponse deliver(Long orderId) {
        return patch(orderServiceUrl + "/api/admin/orders/" + orderId + "/deliver");
    }

    public OrderSummaryResponse cancel(Long orderId) {
        return patch(orderServiceUrl + "/api/admin/orders/" + orderId + "/cancel");
    }


    private OrderSummaryResponse patch(String url) {
        ResponseEntity<OrderSummaryResponse> res = restTemplate.exchange(
                url, HttpMethod.PATCH, HttpEntity.EMPTY, OrderSummaryResponse.class
        );
        return res.getBody();
    }
    public OrderSummaryResponse updateStatus(Long orderId, String status) {
        String url = orderServiceUrl + "/api/admin/orders/" + orderId + "/status";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = Map.of("status", status);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        return restTemplate.exchange(url, HttpMethod.PATCH, entity, OrderSummaryResponse.class).getBody();
    }

}
