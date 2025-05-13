package org.example;

import java.math.BigDecimal;
import java.util.UUID;

public class Store {
    private final String id;
    private String storeName;
    private BigDecimal foodMarkupPercent;
    private BigDecimal nonFoodMarkupPercent;
    private BigDecimal expiryDiscountPercent;
    private int daysBeforeExpirationThreshold;

    public Store(String storeName, BigDecimal foodMarkupPercent, BigDecimal nonFoodMarkupPercent, BigDecimal expiryDiscountPercent, int daysBeforeExpirationThreshold){
        this.id = UUID.randomUUID().toString();
        this.storeName = storeName;
        this.foodMarkupPercent = foodMarkupPercent;
        this.nonFoodMarkupPercent = nonFoodMarkupPercent;
        this.expiryDiscountPercent = expiryDiscountPercent;
        this.daysBeforeExpirationThreshold = daysBeforeExpirationThreshold;
    }
    public String getId(){
        return id;
    }
    public String getStoreName(){
        return storeName;
    }
    public BigDecimal getFoodMarkupPercent(){
        return foodMarkupPercent;
    }
    public BigDecimal getNonFoodMarkupPercent(){
        return nonFoodMarkupPercent;
    }
    public BigDecimal getExpiryDiscountPercent(){
        return expiryDiscountPercent;
    }
    public int getDaysBeforeExpirationThreshold(){
        return daysBeforeExpirationThreshold;
    }

    void setStoreName(String storeName){
        this.storeName = storeName;
    }
    void setFoodMarkupPercent(BigDecimal newFoodMarkupPercent){
        this.foodMarkupPercent = newFoodMarkupPercent;
    }
    void setNonFoodMarkupPercent(BigDecimal newNonFoodMarkupPercent){
        this.nonFoodMarkupPercent = newNonFoodMarkupPercent;
    }
    void setExpiryDiscountPercent(BigDecimal newExpiryDiscountPercent){
        this.expiryDiscountPercent = newExpiryDiscountPercent;
    }
    void setDaysBeforeExpirationThreshold(int daysBeforeExpirationThreshold){
        this.daysBeforeExpirationThreshold = daysBeforeExpirationThreshold;
    }


}
