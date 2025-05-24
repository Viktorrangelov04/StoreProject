package pricing;

import domain.product.Category;

import java.math.BigDecimal;
import java.util.Map;

public class MarkupStrategy {
    private final Map<Category, BigDecimal> markupMap;

    public MarkupStrategy(Map<Category, BigDecimal> markupMap) {
        this.markupMap = markupMap;
    }

    public BigDecimal getMarkup(Category category) {
        BigDecimal markup = markupMap.get(category);
        if (markup == null) {
            throw new IllegalArgumentException("No markup configured for category: " + category);
        }
        return markup;
    }

    public void setMarkup(Category category, BigDecimal newPercent){
        if (newPercent.compareTo(BigDecimal.ZERO) < 0 || newPercent.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new IllegalArgumentException("Markup must be between 0 and 100%");
        }
        markupMap.put(category, newPercent);
    }

    public Map<Category, BigDecimal> getAllMarkups(){
        return Map.copyOf(markupMap);
    }
}
