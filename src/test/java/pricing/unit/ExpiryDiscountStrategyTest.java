package pricing.unit;

import domain.product.StockItem;
import org.junit.jupiter.api.Test;
import pricing.ExpiryDiscountStrategy;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExpiryDiscountStrategyTest {

    @Test
    void applyDiscountIfNeeded_ShouldApplyDiscount_WhenCloseToExpiryAndNotExpired() {
        // Arrange
        StockItem item = mock(StockItem.class);
        when(item.isCloseToExpiry(5)).thenReturn(true);
        when(item.isExpired()).thenReturn(false);
        when(item.getSellingPrice()).thenReturn(new BigDecimal("100.00"));

        ExpiryDiscountStrategy strategy = new ExpiryDiscountStrategy(new BigDecimal("20"), 5);

        // Act
        strategy.applyDiscountIfNeeded(item);

        // Assert
        verify(item).setSellingPrice(new BigDecimal("80.00"));
    }

    @Test
    void applyDiscountIfNeeded_ShouldNotApplyDiscount_WhenNotCloseToExpiry() {
        // Arrange
        StockItem item = mock(StockItem.class);
        when(item.isCloseToExpiry(5)).thenReturn(false);
        when(item.isExpired()).thenReturn(false);

        ExpiryDiscountStrategy strategy = new ExpiryDiscountStrategy(new BigDecimal("20"), 5);

        // Act
        strategy.applyDiscountIfNeeded(item);

        // Assert
        verify(item, never()).setSellingPrice(any());
    }

    @Test
    void applyDiscountIfNeeded_ShouldNotApplyDiscount_WhenExpired() {
        // Arrange
        StockItem item = mock(StockItem.class);
        when(item.isCloseToExpiry(5)).thenReturn(true);
        when(item.isExpired()).thenReturn(true);

        ExpiryDiscountStrategy strategy = new ExpiryDiscountStrategy(new BigDecimal("20"), 5);

        // Act
        strategy.applyDiscountIfNeeded(item);

        // Assert
        verify(item, never()).setSellingPrice(any());
    }

    @Test
    void constructor_ShouldThrow_WhenDiscountOutOfRange() {
        assertThrows(IllegalArgumentException.class,
                () -> new ExpiryDiscountStrategy(new BigDecimal("-1"), 5));
        assertThrows(IllegalArgumentException.class,
                () -> new ExpiryDiscountStrategy(new BigDecimal("101"), 5));
    }
}
