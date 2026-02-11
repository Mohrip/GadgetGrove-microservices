package com.GadgetGrove.order.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class OrderItemRequest {
    private UUID productId;
    private Integer quantity;
}
