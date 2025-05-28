package pricing;

import domain.product.StockItem;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ExpiryDiscountStrategy {
    private final BigDecimal discountPercent;
    private final int daysBeforeExpiryThreshold;

    public ExpiryDiscountStrategy(BigDecimal discountPercent, int daysBeforeExpiryThreshold) {
        if (discountPercent.compareTo(BigDecimal.ZERO) < 0 || discountPercent.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new IllegalArgumentException("Discount must be between 0 and 100%");
        }
        this.discountPercent = discountPercent;
        this.daysBeforeExpiryThreshold = daysBeforeExpiryThreshold;
    }

    public BigDecimal getDiscountPercent() {
        return discountPercent;
    }

    public int getDaysBeforeExpiryThreshold() {
        return daysBeforeExpiryThreshold;
    }

    public void applyDiscountIfNeeded(StockItem item) {
        if (item.isCloseToExpiry(daysBeforeExpiryThreshold)
                && !item.isExpired()) {
            BigDecimal discountRate = discountPercent.movePointLeft(2);

            BigDecimal discountedPrice =
                    item.getSellingPrice()
                            .multiply(BigDecimal.ONE.subtract(discountRate))
                            .setScale(2, RoundingMode.HALF_UP);

            item.setSellingPrice(discountedPrice);
        }
    }
}
