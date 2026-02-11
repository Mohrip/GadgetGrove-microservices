package com.GadgetGrove.order.service;

import com.GadgetGrove.order.dto.*;
import com.GadgetGrove.order.enums.OrderStatus;
import com.GadgetGrove.order.model.Order;
import com.GadgetGrove.order.model.OrderItem;
import com.GadgetGrove.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    @Value("${product.service.url:http://localhost:8082}")
    private String productServiceUrl;

    @Transactional
    public OrderResponse placeOrder(UUID userId, OrderRequest request) {
        if (request.getOrderItems() == null || request.getOrderItems().isEmpty()) {
            throw new RuntimeException("Order items cannot be empty");
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setShippingAddress(request.getShippingAddress());
        order.setStatus(OrderStatus.PENDING);

        List<OrderItem> orderItems = request.getOrderItems().stream()
                .map(itemRequest -> {
                    ProductResponse product = webClientBuilder.build()
                            .get()
                            .uri(productServiceUrl + "/api/products/{id}", itemRequest.getProductId())
                            .retrieve()
                            .bodyToMono(ProductResponse.class)
                            .block();

                    if (product == null) {
                        throw new RuntimeException("Product not found: " + itemRequest.getProductId());
                    }
                    if (product.getStockQuantity() < itemRequest.getQuantity()) {
                        throw new RuntimeException("Insufficient stock for product: " + product.getName());
                    }

                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setProductId(product.getId().toString());
                    orderItem.setQuantity(itemRequest.getQuantity());
                    orderItem.setPrice(product.getPrice());
                    return orderItem;
                }).collect(Collectors.toList());

        order.setOrderItems(orderItems);
        BigDecimal totalAmount = orderItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);
        return mapToResponse(savedOrder);
    }

    private OrderResponse mapToResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setUserId(order.getUserId());
        response.setTotalAmount(order.getTotalAmount());
        response.setStatus(order.getStatus());
        response.setShippingAddress(order.getShippingAddress());
        response.setCreatedAt(order.getCreatedAt());

        List<OrderItemResponse> itemResponses = order.getOrderItems().stream()
                .map(item -> {
                    OrderItemResponse itemResponse = new OrderItemResponse();
                    itemResponse.setProductId(UUID.fromString(item.getProductId()));
                    itemResponse.setQuantity(item.getQuantity());
                    itemResponse.setPrice(item.getPrice());
                    return itemResponse;
                }).collect(Collectors.toList());

        response.setOrderItems(itemResponses);
        return response;
    }
}
