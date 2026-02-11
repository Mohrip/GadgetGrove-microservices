package com.GadgetGrove.cart.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CartItemResponse {
    private UUID id;
    private UUID userId;
    private UUID productId;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
    private LocalDateTime createdAt;
}

