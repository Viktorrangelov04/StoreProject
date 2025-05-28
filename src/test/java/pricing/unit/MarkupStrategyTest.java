package pricing.unit;

import domain.product.Category;
import org.junit.jupiter.api.Test;
import pricing.MarkupStrategy;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MarkupStrategyTest {
    @Test
    void getMarkup_ShouldReturnMarkup(){
        Map<Category, BigDecimal> markups = new EnumMap<>(Category.class);
        markups.put(Category.Food, new BigDecimal("20"));
        MarkupStrategy strategy = new MarkupStrategy(markups);

        assertEquals(new BigDecimal("20"), strategy.getMarkup(Category.Food));
    }

    @Test
    void getMarkup_ShouldThrow_WhenCategoryNotConfigured() {
        MarkupStrategy strategy = new MarkupStrategy(new EnumMap<>(Category.class));
        assertThrows(IllegalArgumentException.class, () -> strategy.getMarkup(Category.Food));
    }

    @Test
    void setMarkup_ShouldUpdateMarkup() {
        Map<Category, BigDecimal> markups = new EnumMap<>(Category.class);
        markups.put(Category.Food, new BigDecimal("20"));
        MarkupStrategy strategy = new MarkupStrategy(markups);

        strategy.setMarkup(Category.Food, new BigDecimal("30"));
        assertEquals(new BigDecimal("30"), strategy.getMarkup(Category.Food));
    }

    @Test
    void setMarkup_ShouldThrow_WhenOutOfRange() {
        Map<Category, BigDecimal> markups = new EnumMap<>(Category.class);
        markups.put(Category.Food, new BigDecimal("20"));
        MarkupStrategy strategy = new MarkupStrategy(markups);

        assertThrows(IllegalArgumentException.class, () -> strategy.setMarkup(Category.Food, new BigDecimal("-1")));
        assertThrows(IllegalArgumentException.class, () -> strategy.setMarkup(Category.Food, new BigDecimal("101")));
    }

    @Test
    void calculateSellingPrice_ShouldReturnCorrectPrice() {
        Map<Category, BigDecimal> markups = new EnumMap<>(Category.class);
        markups.put(Category.Food, new BigDecimal("20"));
        MarkupStrategy strategy = new MarkupStrategy(markups);

        BigDecimal deliveryPrice = new BigDecimal("100");
        BigDecimal expected = new BigDecimal("120.00");
        assertEquals(expected, strategy.calculateSellingPrice(deliveryPrice, Category.Food));
    }

    @Test
    void getAllMarkups_ShouldReturnUnmodifiableCopy() {
        Map<Category, BigDecimal> markups = new EnumMap<>(Category.class);
        markups.put(Category.Food, new BigDecimal("20"));
        MarkupStrategy strategy = new MarkupStrategy(markups);

        Map<Category, BigDecimal> allMarkups = strategy.getAllMarkups();
        assertEquals(new BigDecimal("20"), allMarkups.get(Category.Food));
        assertThrows(UnsupportedOperationException.class, () -> allMarkups.put(Category.Food, new BigDecimal("30")));
    }
}