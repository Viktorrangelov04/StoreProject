package Products;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FoodProduct extends Product{
    private LocalDate expiryDate;

    public FoodProduct(String name, BigDecimal deliveryPrice, Category category, LocalDate expiryDate) {
        super(name, deliveryPrice, category);
        this.expiryDate = expiryDate;
    }

    public LocalDate getExpiryDate(){
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate){
        this.expiryDate = expiryDate;
    }

}
