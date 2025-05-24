package pricing;

import domain.product.Category;
import domain.product.StockItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

public class PricingStrategy {
    private final Map<Category, BigDecimal> markupPercentages;

    public PricingStrategy(Map<Category, BigDecimal> markupPercentages) {
        this.markupPercentages = markupPercentages;
    }

    public BigDecimal calculateSellingPrice(StockItem item) {
        Category category = item.getProduct().getCategory();
        BigDecimal markup = markupPercentages.get(category);
        if (markup == null) {
            throw new IllegalArgumentException("Missing markup configuration for category: " + category);
        }

        BigDecimal rate = markup.divide(BigDecimal.valueOf(100));
        return item.getDeliveryPrice()
                .multiply(BigDecimal.ONE.add(rate))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
