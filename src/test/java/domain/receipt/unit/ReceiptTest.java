package domain.receipt.unit;

import domain.product.Product;
import domain.product.StockItem;
import domain.receipt.Receipt;
import domain.store.Cashier;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReceiptTest {

    @Test
    void getSerialNumber_ShouldReturnSerialNumber() {
        Cashier cashier = mock(Cashier.class);
        when(cashier.getName()).thenReturn("Ivan");

        Receipt receipt = new Receipt(123, cashier, Collections.emptyMap(), 0, BigDecimal.ZERO);

        assertEquals(123, receipt.getSerialNumber());
    }

    @Test
    void getCashier_ShouldReturnCashierName() {
        Cashier cashier = mock(Cashier.class);
        when(cashier.getName()).thenReturn("Maria");

        Receipt receipt = new Receipt(1, cashier, Collections.emptyMap(), 0, BigDecimal.ZERO);

        assertEquals("Maria", receipt.getCashier());
    }

    @Test
    void getProducts_ShouldReturnProductsMap() {
        Cashier cashier = mock(Cashier.class);
        when(cashier.getName()).thenReturn("Maria");

        StockItem item = mock(StockItem.class);
        Map<StockItem, Integer> products = Collections.singletonMap(item, 2);

        Receipt receipt = new Receipt(1, cashier, products, 2, new BigDecimal("10.00"));

        assertEquals(products, receipt.getProducts());
    }

    @Test
    void formatReceipt_ShouldContainProductNameAndPrice() {
        // Arrange
        Cashier cashier = mock(Cashier.class);
        when(cashier.getName()).thenReturn("Maria");

        Product product = mock(Product.class);
        when(product.getName()).thenReturn("banana");

        StockItem item = mock(StockItem.class);
        when(item.getProduct()).thenReturn(product);
        when(item.getSellingPrice()).thenReturn(new BigDecimal("5.00"));

        Map<StockItem, Integer> products = Collections.singletonMap(item, 2);

        Receipt receipt = new Receipt(1, cashier, products, 2, new BigDecimal("10.00"));

        // Act
        String formatted = receipt.formatReceipt();

        // Assert
        assertTrue(formatted.contains("banana"));
        assertTrue(formatted.contains("5.00"));
        assertTrue(formatted.contains("10.00"));
    }

    @Test
    void getTimestamp_ShouldReturnTimestamp() {
        Cashier cashier = mock(Cashier.class);
        when(cashier.getName()).thenReturn("Maria");

        LocalDateTime now = LocalDateTime.now();
        Receipt receipt = new Receipt(1, cashier, Collections.emptyMap(), 0, BigDecimal.ZERO, now);

        assertEquals(now, receipt.getTimestamp());
    }
}
