package com.GadgetGrove.cart.service;

import com.GadgetGrove.cart.dto.CartItemResponse;
import com.GadgetGrove.cart.dto.CartItemrequest;
import com.GadgetGrove.cart.dto.ProductResponse;
import com.GadgetGrove.cart.model.CartItem;
import com.GadgetGrove.cart.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final WebClient.Builder webClientBuilder;

    @Value("${product.service.url}")
    private String productServiceUrl;

    public void addToCart(UUID userId, CartItemrequest request) {
        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new RuntimeException("Quantity must be greater than zero");
        }

        // Call Product service to get product details
        ProductResponse product = webClientBuilder.build()
                .get()
                .uri(productServiceUrl + "/api/products/{id}", request.getProductId())
                .retrieve()
                .bodyToMono(ProductResponse.class)
                .block();

        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        if (request.getQuantity() > product.getStockQuantity()) {
            throw new RuntimeException("Insufficient stock available");
        }

        // Check if item already exists in cart
        Optional<CartItem> existingItemOpt = cartItemRepository.findByUserIdAndProductId(userId, request.getProductId());

        CartItem cartItem;
        if (existingItemOpt.isPresent()) {
            cartItem = existingItemOpt.get();
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
            cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        } else {
            cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(request.getProductId());
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
        }

        cartItemRepository.save(cartItem);
    }

    public List<CartItemResponse> getCartItemsByUserId(UUID userId) {
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);

        return cartItems.stream().map(item -> {
            CartItemResponse response = new CartItemResponse();
            response.setId(item.getId());
            response.setUserId(item.getUserId());
            response.setProductId(item.getProductId());
            response.setQuantity(item.getQuantity());
            response.setPrice(item.getPrice());
            response.setCreatedAt(item.getCreatedAt());

            // Optionally fetch product name from product service
            try {
                ProductResponse product = webClientBuilder.build()
                        .get()
                        .uri(productServiceUrl + "/api/products/{id}", item.getProductId())
                        .retrieve()
                        .bodyToMono(ProductResponse.class)
                        .block();
                if (product != null) {
                    response.setProductName(product.getName());
                }
            } catch (Exception e) {
                response.setProductName("Unknown");
            }

            return response;
        }).collect(Collectors.toList());
    }

    public void deleteCartItem(UUID cartItemId) {
        if (!cartItemRepository.existsById(cartItemId)) {
            throw new RuntimeException("Cart item not found");
        }
        cartItemRepository.deleteById(cartItemId);
    }

    public void removeItemFromCart(UUID userId, UUID productId) {
        CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new RuntimeException("Cart item not found for the given user and product"));
        cartItemRepository.delete(cartItem);
    }

    public void clearCart(UUID userId) {
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        cartItemRepository.deleteAll(cartItems);
    }
}
