package com.GadgetGrove.cart.mapper;

import com.GadgetGrove.cart.dto.CartItemResponse;
import com.GadgetGrove.cart.model.CartItem;
import org.springframework.stereotype.Component;

@Component
public class CartItemMapper {

    public CartItemResponse toResponse(CartItem item) {
        CartItemResponse response = new CartItemResponse();
        response.setId(item.getId());
        response.setUserId(item.getUserId());
        response.setProductId(item.getProductId());
        response.setQuantity(item.getQuantity());
        response.setPrice(item.getPrice());
        response.setCreatedAt(item.getCreatedAt());
        return response;
    }
}
