package Products;

import java.math.BigDecimal;
import java.util.UUID;
import java.time.LocalDate;

public class Product {
    private final String id;
    private String name;
    private BigDecimal deliveryPrice;
    private final Category category;
    private final LocalDate expiryDate;

    public Product(String name, BigDecimal deliveryPrice, Category category, LocalDate expiryDate) {
        this.id =  UUID.randomUUID().toString();
        this.name = name;
        this.deliveryPrice = deliveryPrice;
        this.category = category;
        this.expiryDate = expiryDate;
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
    public LocalDate getExpiryDate(){
        return expiryDate;
    }

//    public BigDecimal getSellingPrice(){
//        BigDecimal markup= BigDecimal.valueOf(category.getMarkup());
//        BigDecimal multiplier = BigDecimal.ONE.add(markup);
//        return deliveryPrice.multiply(multiplier);
//    }

    void setName(String newName){
        this.name = newName;
    }
    void setDeliveryPrice(BigDecimal newDeliveryPrice){
        this.deliveryPrice = newDeliveryPrice;
    }
}
