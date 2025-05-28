package domain.product;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class StockItem implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Product product;
    private BigDecimal deliveryPrice;
    private BigDecimal sellingPrice;
    private int quantity;
    private final LocalDate expiryDate;

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

    public void addQuantity(int amount) {
        if (amount < 0) throw new IllegalArgumentException("Cannot add negative quantity");
        this.quantity += amount;
    }

    public void removeQuantity(int amount) {
        if (amount < 0) throw new IllegalArgumentException("Cannot remove negative quantity");

        if (amount > quantity) {
            throw new IllegalArgumentException("Not enough stock to remove " + amount + "you only have " + quantity);
        }
        this.quantity -= amount;
    }

    public void setQuantity(int newQuantity){
        quantity = newQuantity;
    }

    public boolean isExpired() {
        LocalDate now = LocalDate.now();
        return expiryDate.isBefore(now);
    }

    public boolean isCloseToExpiry(int thresholdDays){
        return ChronoUnit.DAYS.between(LocalDate.now(), expiryDate)<=thresholdDays;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StockItem that)) return false;
        return quantity == that.quantity &&
                product.equals(that.product) &&
                deliveryPrice.equals(that.deliveryPrice) &&
                expiryDate.equals(that.expiryDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, deliveryPrice, quantity, expiryDate);
    }
}
