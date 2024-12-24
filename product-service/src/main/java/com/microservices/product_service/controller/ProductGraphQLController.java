package com.microservices.product_service.controller;


import com.microservices.product_service.dto.ProductResponse;
import com.microservices.product_service.dto.UpdateProductRequest;
import com.microservices.product_service.model.Product;
import com.microservices.product_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductGraphQLController {
    @Autowired
    private ProductService productService;

    @QueryMapping
    public Product getProduct(@Argument String id) {
        return productService.getProductById(id);
    }

    @QueryMapping
    public List<ProductResponse> listProducts() {
        return productService.getAllProducts();
    }

    @MutationMapping
    public Product addProduct(@Argument ProductInput input) {
        Product product = new Product();
        product.setName(input.getName());
        product.setDescription(input.getDescription());
        product.setPrice(input.getPrice());
        return productService.addProduct(product);
    }

    @MutationMapping
    public ProductResponse updateProduct(@Argument UpdateProductInput input) {
        UpdateProductRequest product = new UpdateProductRequest(input.id(), input.name(), input.description(), input.price());
        return productService.updateProduct(product);
    }

    @MutationMapping
    public void deleteProduct(@Argument String id) {
        productService.deleteProduct(id);
    }
}
