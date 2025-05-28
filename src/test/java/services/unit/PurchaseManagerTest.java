package services.unit;

import domain.product.StockItem;
import org.junit.jupiter.api.Test;
import services.PurchaseManager;
import services.InventoryManager;
import storage.ReceiptStorage;
import services.FinancialManager;
import pricing.ExpiryDiscountStrategy;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PurchaseManagerTest {

    @Test
    void addToCart_ShouldAddItemWithCorrectQuantity() {
        InventoryManager inventoryManager = mock(InventoryManager.class);
        ReceiptStorage receiptStorage = mock(ReceiptStorage.class);
        FinancialManager financialManager = mock(FinancialManager.class);
        ExpiryDiscountStrategy expiryDiscountStrategy = mock(ExpiryDiscountStrategy.class);

        PurchaseManager manager = new PurchaseManager(
                inventoryManager, receiptStorage, financialManager, expiryDiscountStrategy
        );

        StockItem item = mock(StockItem.class);

        manager.addToCart(item, 3);

        assertEquals(3, manager.getCart().get(item));
    }

    @Test
    void addToCart_ShouldThrow_WhenQuantityIsZeroOrNegative() {
        InventoryManager inventoryManager = mock(InventoryManager.class);
        ReceiptStorage receiptStorage = mock(ReceiptStorage.class);
        FinancialManager financialManager = mock(FinancialManager.class);
        ExpiryDiscountStrategy expiryDiscountStrategy = mock(ExpiryDiscountStrategy.class);

        PurchaseManager manager = new PurchaseManager(
                inventoryManager, receiptStorage, financialManager, expiryDiscountStrategy
        );

        StockItem item = mock(StockItem.class);

        assertThrows(IllegalArgumentException.class, () -> manager.addToCart(item, 0));
        assertThrows(IllegalArgumentException.class, () -> manager.addToCart(item, -1));
    }
}
