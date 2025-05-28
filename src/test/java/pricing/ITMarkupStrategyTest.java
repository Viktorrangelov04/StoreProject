package pricing;

import domain.product.Category;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ITMarkupStrategyTest {

    @Test
    void getMarkup() {
        //Arrange
        Map<Category, BigDecimal> markupPercentages = Map.of(
                Category.Food, new BigDecimal("30"),
                Category.NonFood, new BigDecimal("20")
        );

        //Act
        MarkupStrategy strategy = new MarkupStrategy(markupPercentages);

        //Assert
        assertEquals(new BigDecimal("30"), strategy.getMarkup(Category.Food));
    }

    @Test
    void setMarkup() {
        //Arrange
        Map<Category, BigDecimal> markupPercentages = new HashMap<>();
        markupPercentages.put(Category.Food, new BigDecimal("30"));
        markupPercentages.put(Category.NonFood, new BigDecimal("20"));
        MarkupStrategy strategy = new MarkupStrategy(markupPercentages);

        //Act
        strategy.setMarkup(Category.Food, new BigDecimal("40"));

        //Assert
        assertEquals(new BigDecimal("40"), strategy.getMarkup(Category.Food));
    }

    @Test
    void getAllMarkups_ShouldReturnAllMarkups() {
        //Arrange
        Map<Category, BigDecimal> markupPercentages = Map.of(
                Category.Food, new BigDecimal("30"),
                Category.NonFood, new BigDecimal("20")
        );
        MarkupStrategy strategy = new MarkupStrategy(markupPercentages);

        //Assert
        Map<Category, BigDecimal> expected = Map.of(
                Category.Food, new BigDecimal("30"),
                Category.NonFood, new BigDecimal("20")
        );

        assertEquals(expected, strategy.getAllMarkups());
    }

    @Test
    void calculateSellingPrice_ShouldReturnSellingPrice() {
        Map<Category, BigDecimal> markupPercentages = Map.of(
                Category.Food, new BigDecimal("30"),
                Category.NonFood, new BigDecimal("20")
        );
        MarkupStrategy strategy = new MarkupStrategy(markupPercentages);

        //Assert
        assertEquals(new BigDecimal("26.00"), strategy.calculateSellingPrice(new BigDecimal("20"), Category.Food));
    }
}