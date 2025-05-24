package pricing;

import domain.product.Category;

import java.math.BigDecimal;

public interface PricingStrategy {
    BigDecimal calculateSellingPrice(BigDecimal deliveryPrice, Category category);
}
