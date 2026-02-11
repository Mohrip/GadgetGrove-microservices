package com.GadgetGrove.cart;

import lombok.Data;

import java.util.UUID;

@Data
public class CartItemrequest {
    private UUID productId;
    private Integer quantity;
}
