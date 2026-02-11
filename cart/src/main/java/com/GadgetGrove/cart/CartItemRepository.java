package com.GadgetGrove.cart;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, UUID> {

    Optional<CartItem> findByUserIdAndProductId(UUID userId, UUID productId);

    List<CartItem> findByUserId(UUID userId);


}
