package com.GadgetGrove.cart.controller;

import com.GadgetGrove.cart.dto.CartItemResponse;
import com.GadgetGrove.cart.dto.CartItemrequest;
import com.GadgetGrove.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<Void> addToCart(@RequestHeader("User-ID") UUID userId, @RequestBody CartItemrequest request) {
        cartService.addToCart(userId, request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> getCart(@RequestHeader("User-ID") UUID userId) {
        List<CartItemResponse> cartItems = cartService.getCartItemsByUserId(userId);
        return ResponseEntity.ok(cartItems);
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable UUID cartItemId) {
        cartService.deleteCartItem(cartItemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/item/{productId}")
    public ResponseEntity<Void> removeItemFromCart(
            @RequestHeader("User-ID") UUID userId,
            @PathVariable UUID productId) {
        cartService.removeItemFromCart(userId, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(@RequestHeader("User-ID") UUID userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}
