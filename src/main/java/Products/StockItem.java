package Products;

import java.math.BigDecimal;
import java.time.LocalDate;

public class StockItem {
    private BigDecimal deliveryPrice;
    private BigDecimal sellingPrice;
    private int quantity;
    private LocalDate expiryDate;

    public StockItem(BigDecimal deliveryPrice, int quantity, LocalDate expiryDate) {
        this.deliveryPrice = deliveryPrice;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
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
    public void setQuantity(int newQuantity){
        quantity = newQuantity;
    }
    public void setExpiryDate(LocalDate newExpiryDate){
        expiryDate = newExpiryDate;
    }
}
