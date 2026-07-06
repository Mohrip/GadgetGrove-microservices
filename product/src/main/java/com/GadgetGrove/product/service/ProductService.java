package com.GadgetGrove.product.service;

import com.GadgetGrove.product.dto.ProductRequest;
import com.GadgetGrove.product.dto.ProductResponse;
import com.GadgetGrove.product.mapper.ProductMapper;
import com.GadgetGrove.product.model.Product;
import com.GadgetGrove.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = productMapper.toEntity(productRequest);
        Product savedProduct = productRepository.save(product);
        return productMapper.toResponse(savedProduct);
    }

    public ProductResponse updateProduct(UUID id, ProductRequest productRequest) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));

        productMapper.updateEntity(product, productRequest);

        Product savedProduct = productRepository.save(product);
        return productMapper.toResponse(savedProduct);
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse getProductById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        return productMapper.toResponse(product);
    }

    public void deleteProduct(UUID id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    public List<ProductResponse> searchProductsByName(String name) {
        return productRepository.findAll().stream()
                .filter(product -> product.getName() != null && product.getName().toLowerCase().contains(name.toLowerCase()))
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    // Custom exception class
    public static class ProductNotFoundException extends RuntimeException {
        public ProductNotFoundException(String message) {
            super(message);
        }
    }
}
