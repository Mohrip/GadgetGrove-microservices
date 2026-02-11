package com.GadgetGrove.order.model;

import com.GadgetGrove.order.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID userId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    private String shippingAddress;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
