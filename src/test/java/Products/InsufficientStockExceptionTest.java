package Products;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class InsufficientStockExceptionTest {
    @Test
    public void testThrowingInsufficientStockException() {
        Product product = new Product("Cheese", Category.Food);
        StockItem stockItem = new StockItem(product, new BigDecimal("3.40"), 2, LocalDate.now().plusDays(2));

        assertThrows(InsufficientStockException.class, () -> {
                throw new InsufficientStockException(stockItem, 5, 2);
        });
    }

}