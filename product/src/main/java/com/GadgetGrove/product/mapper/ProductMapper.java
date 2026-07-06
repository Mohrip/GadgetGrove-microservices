package com.GadgetGrove.product.mapper;

import com.GadgetGrove.product.dto.ProductRequest;
import com.GadgetGrove.product.dto.ProductResponse;
import com.GadgetGrove.product.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequest request) {
        Product product = new Product();
        applyRequest(product, request);
        return product;
    }

    public void updateEntity(Product product, ProductRequest request) {
        applyRequest(product, request);
    }

    public ProductResponse toResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setStockQuantity(product.getStockQuantity());
        response.setCategory(product.getCategory());
        response.setImageUrl(product.getImageUrl());
        response.setActive(product.getActive());
        return response;
    }

    private void applyRequest(Product product, ProductRequest request) {
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setCategory(request.getCategory());
        product.setImageUrl(request.getImageUrl());
        product.setActive(request.getActive());
    }
}
