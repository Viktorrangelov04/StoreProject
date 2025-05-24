package pricing;

import domain.product.Category;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

public class MarkupStrategy implements PricingStrategy {
    private final Map<Category, BigDecimal> markupPercentages;

    public MarkupStrategy(Map<Category, BigDecimal> markupMap) {
        this.markupPercentages = markupMap;
    }

    public BigDecimal getMarkup(Category category) {
        BigDecimal markup = markupPercentages.get(category);
        if (markup == null) {
            throw new IllegalArgumentException("No markup configured for category: " + category);
        }
        return markup;
    }

    public void setMarkup(Category category, BigDecimal newPercent){
        if (newPercent.compareTo(BigDecimal.ZERO) < 0 || newPercent.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new IllegalArgumentException("Markup must be between 0 and 100%");
        }
        markupPercentages.put(category, newPercent);
    }

    public Map<Category, BigDecimal> getAllMarkups(){
        return Map.copyOf(markupPercentages);
    }

    @Override
    public BigDecimal calculateSellingPrice(BigDecimal deliveryPrice, Category category) {
        BigDecimal markup = markupPercentages.getOrDefault(category, BigDecimal.ZERO);
        BigDecimal markupRate = markup.divide(new BigDecimal("100"));
        return deliveryPrice.multiply(BigDecimal.ONE.add(markupRate))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
