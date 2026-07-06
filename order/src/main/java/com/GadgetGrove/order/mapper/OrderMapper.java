package com.GadgetGrove.order.mapper;

import com.GadgetGrove.order.dto.OrderItemResponse;
import com.GadgetGrove.order.dto.OrderResponse;
import com.GadgetGrove.order.dto.ProductResponse;
import com.GadgetGrove.order.model.Order;
import com.GadgetGrove.order.model.OrderItem;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderItem toOrderItem(Order order, ProductResponse product, Integer quantity) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProductId(product.getId().toString());
        orderItem.setQuantity(quantity);
        orderItem.setPrice(product.getPrice());
        return orderItem;
    }

    public OrderResponse toResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setUserId(order.getUserId());
        response.setTotalAmount(order.getTotalAmount());
        response.setStatus(order.getStatus());
        response.setShippingAddress(order.getShippingAddress());
        response.setCreatedAt(order.getCreatedAt());
        response.setOrderItems(order.getOrderItems().stream()
                .map(this::toItemResponse)
                .collect(Collectors.toList()));
        return response;
    }

    private OrderItemResponse toItemResponse(OrderItem item) {
        OrderItemResponse itemResponse = new OrderItemResponse();
        itemResponse.setProductId(UUID.fromString(item.getProductId()));
        itemResponse.setQuantity(item.getQuantity());
        itemResponse.setPrice(item.getPrice());
        return itemResponse;
    }
}
