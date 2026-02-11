package com.GadgetGrove.order.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private String shippingAddress;
    private List<OrderItemRequest> orderItems;
}
