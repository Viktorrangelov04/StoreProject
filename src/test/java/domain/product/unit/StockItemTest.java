package domain.product.unit;

import domain.product.Product;
import domain.product.StockItem;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

public class StockItemTest {
    @Test
    void getProduct_ShouldReturnMockedProduct() {
        // Arrange
        Product mockProduct = mock(Product.class);
        when(mockProduct.getName()).thenReturn("MockedProduct");

        StockItem item = new StockItem(
                mockProduct,
                new BigDecimal("20"),
                10,
                LocalDate.now().plusDays(10)
        );

        //Assert
        assertSame(mockProduct, item.getProduct());
        assertEquals("MockedProduct", item.getProduct().getName());

        verify(mockProduct, times(1)).getName();
    }
}
