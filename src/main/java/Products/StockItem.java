package Products;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class StockItem {
    private Product product;
    private BigDecimal deliveryPrice;
    private BigDecimal sellingPrice;
    private int quantity;
    private LocalDate expiryDate;

    public StockItem(Product product, BigDecimal deliveryPrice, int quantity, LocalDate expiryDate) {
        this.product = product;
        this.deliveryPrice = deliveryPrice;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
    }

    public Product getProduct(){
        return product;
    }
    public BigDecimal getDeliveryPrice(){
        return deliveryPrice;
    }
    public BigDecimal getSellingPrice(){
        return sellingPrice;
    }
    public int getQuantity(){
        return quantity;
    }
    public LocalDate getExpiryDate(){
        return expiryDate;
    }

    public void setDeliveryPrice(BigDecimal newDeliveryPrice){
        deliveryPrice = newDeliveryPrice;
    }
    public void setSellingPrice(BigDecimal newSellingPrice){
        sellingPrice = newSellingPrice;
    }
    public void applyCloseToExpiryDiscount(BigDecimal discountPercentage){
        BigDecimal discountRate = discountPercentage.divide(new BigDecimal("100"));
        BigDecimal multiplier = BigDecimal.ONE.subtract(discountRate);

        this.sellingPrice = sellingPrice.multiply(multiplier).setScale(2, RoundingMode.HALF_UP);
    }

    public void setQuantity(int newQuantity){
        quantity = newQuantity;
    }
    public void setExpiryDate(LocalDate newExpiryDate){
        expiryDate = newExpiryDate;
    }

    public boolean isExpired() {
        LocalDate now = LocalDate.now();
        return expiryDate.isBefore(now);
    }

    public boolean isCloseToExpiry(int thresholdDays){
        return ChronoUnit.DAYS.between(LocalDate.now(), expiryDate)<=thresholdDays;
    }
}
