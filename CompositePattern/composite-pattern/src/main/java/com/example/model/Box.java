package com.example.model;

import java.util.List;
import java.util.UUID;

import com.example.model.Product;

public class Box implements Product {

    private List<Product> products;
    private String id;

    public Box(List<Product> products) {
        this.products = products;
        this.id = UUID.randomUUID().toString();
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getId() {
        return id;
    }

    public List<Product> getProducts() {
        return products;
    }

    @Override
    public long calculateSum() {
        return this.products.stream().map(
            product -> {
                return product.calculateSum();
            }
        ).reduce(0L, Long::sum);
    }
    
}
