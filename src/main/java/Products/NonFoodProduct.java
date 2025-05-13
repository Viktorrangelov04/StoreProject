package Products;

import java.math.BigDecimal;

public class NonFoodProduct extends Product{
    public NonFoodProduct(String name, BigDecimal deliveryPrice, Category category){
        super(name, deliveryPrice, category);
    }
}
