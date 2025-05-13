package org.example;

import java.math.BigDecimal;
import java.util.UUID;
//import java.time.LocalDate;

public abstract class Product {
    private final String id;
    private final String name;
    private final BigDecimal deliveryPrice;
    private final Category category;
//    private final LocalDate expiryDate;

    public Product(String name, BigDecimal deliveryPrice, Category category) {
        this.id =  UUID.randomUUID().toString();
        this.name = name;
        this.deliveryPrice = deliveryPrice;
        this.category = category;
    }

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }
    public BigDecimal getDeliveryPrice(){
        return deliveryPrice;
    }
    public Category getCategory(){
        return category;
    }

    public BigDecimal getSellingPrice(){
        BigDecimal markup= BigDecimal.valueOf(category.getMarkup());
        BigDecimal multiplier = BigDecimal.ONE.add(markup);
        return deliveryPrice.multiply(multiplier);
    }
}
