package pricing;

import domain.product.Category;
import domain.product.Product;
import domain.product.StockItem;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ExpiryDiscountStrategyTest {

    @Test
    void getDiscountPercent() {
        //Arrange
        ExpiryDiscountStrategy expiryDiscountStrategy = new ExpiryDiscountStrategy(new BigDecimal("15"), 7);
        //Assert
        assertEquals(new BigDecimal("15"), expiryDiscountStrategy.getDiscountPercent());
    }

    @Test
    void getDaysBeforeExpiryThreshold() {
        //Arrange
        ExpiryDiscountStrategy expiryDiscountStrategy = new ExpiryDiscountStrategy(new BigDecimal("15"), 7);
        //Assert
        assertEquals(7, expiryDiscountStrategy.getDaysBeforeExpiryThreshold());
    }

    @Test
    void applyDiscountIfNeeded() {
        // Arrange
        StockItem item = new StockItem(new Product("Apple", Category.Food),
                new BigDecimal("20"), 2, LocalDate.now().plusDays(5));
        item.setSellingPrice(new BigDecimal("24.00"));

        ExpiryDiscountStrategy strategy = new ExpiryDiscountStrategy(new BigDecimal("15"), 7);

        // Act
        strategy.applyDiscountIfNeeded(item);

        // Assert
        BigDecimal expected = new BigDecimal("24.00")
                .multiply(BigDecimal.ONE.subtract(new BigDecimal("15").divide(new BigDecimal("100"))))
                .setScale(2, RoundingMode.HALF_UP);

        assertEquals(expected, item.getSellingPrice());
    }

    @Test
    void applyDiscount_ShouldntApply_IfNotNeeded(){
        //Arrange
        StockItem item = new StockItem(new Product("Apple", Category.Food),
                new BigDecimal("20"), 2, LocalDate.now().plusDays(10));
        item.setSellingPrice(new BigDecimal("24.00"));

        ExpiryDiscountStrategy strategy = new ExpiryDiscountStrategy(new BigDecimal("15"), 7);

        //Act
        strategy.applyDiscountIfNeeded(item);

        //Assert
        assertEquals(new BigDecimal("24.00"), item.getSellingPrice());
    }
}