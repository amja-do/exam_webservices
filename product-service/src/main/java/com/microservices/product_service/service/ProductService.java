package com.microservices.product_service.service;

import com.microservices.product_service.dto.ProductRequest;
import com.microservices.product_service.dto.ProductResponse;
import com.microservices.product_service.dto.UpdateProductRequest;
import com.microservices.product_service.model.Product;
import com.microservices.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.name())
                .description(productRequest.description())
                .price(productRequest.price())
                .build();

        productRepository.save(product);
        log.info("Product {} is saved", product.getId());
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream().map(this::mapToProductResponse).toList();
    }

    public Product getProductById(String id){
        return productRepository.findById(id).get();
    }

    public ProductResponse updateProduct(UpdateProductRequest request){
        Product product = this.getProductById(request.id());
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        return this.mapToProductResponse(productRepository.save(product));
    }

    private ProductResponse mapToProductResponse(Product product) {
        return new ProductResponse(product.getId(), product.getName(),
                product.getDescription(), product.getPrice());
    }

    public void deleteProduct(String id){
        productRepository.deleteById(id);
    }
}