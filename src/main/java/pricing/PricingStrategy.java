package pricing;

import domain.product.StockItem;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PricingStrategy {
    private final MarkupStrategy markupStrategy;

    public PricingStrategy(MarkupStrategy markupStrategy) {
        this.markupStrategy = markupStrategy;
    }

    public BigDecimal calculateSellingPrice(StockItem item) {
        BigDecimal markup = markupStrategy.getMarkup(item.getProduct().getCategory());
        if (markup == null) {
            throw new IllegalArgumentException("Missing markup configuration for category: " + item.getProduct().getCategory());
        }

        BigDecimal rate = markup.divide(BigDecimal.valueOf(100));
        return item.getDeliveryPrice()
                .multiply(BigDecimal.ONE.add(rate))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
