package com.microservices.order_service.service;

import com.microservices.order_service.model.Order;
import com.microservices.order_service.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private WebClient webClient;

    @CircuitBreaker(name = "productService", fallbackMethod = "fallbackProductValidation")
    public boolean validateProduct(String productId) {
        String query = "{ getProduct(id: \"" + productId + "\") { id name price } }";

        Map<String, Object> response = webClient.post()
                .bodyValue(Map.of("query", query))
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        Map<String, Object> data = (Map<String, Object>) response.get("data");
        return data != null && data.get("getProduct") != null;
    }

    public boolean fallbackProductValidation(String productId, Throwable throwable) {
        System.out.println("Fallback called for product validation. Error: " + throwable.getMessage());
        return false;
    }

    public Order createOrder(Order order) {
        if (!validateProduct(order.getProductId())) {
            throw new RuntimeException("Product validation failed");
        }

        Order savedOrder = orderRepository.save(order);

        kafkaTemplate.send("order-events", "Order Created: " + savedOrder.getId());
        return savedOrder;
    }
}

