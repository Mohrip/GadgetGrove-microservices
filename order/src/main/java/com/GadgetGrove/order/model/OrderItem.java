package com.GadgetGrove.order.model;

//import com.GadgetGrove.GadgetGrove.product.Product;
//import com.GadgetGrove.product.model.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    //@ManyToOne
    //@JoinColumn(name = "product_id", nullable = false)
    //private Product product;
    private String productId;

    private Integer quantity;

    private BigDecimal price;
}
