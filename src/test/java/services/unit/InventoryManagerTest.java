package services.unit;

import domain.product.Category;
import domain.product.Product;
import domain.product.StockItem;
import org.junit.jupiter.api.Test;
import services.InventoryManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InventoryManagerTest {

    @Test
    void addStock_ShouldAddItemToInventory() {
        InventoryManager manager = new InventoryManager();
        Product product = new Product("Apple", Category.Food);
        StockItem item = new StockItem(product, new BigDecimal("10.00"), 5, LocalDate.now().plusDays(10));

        manager.addStock(item);

        List<StockItem> stock = manager.getStockForProduct(product);
        assertTrue(stock.contains(item));
    }

    @Test
    void getInventory_ShouldReturnCopy() {
        InventoryManager manager = new InventoryManager();
        Product product = new Product("Apple", Category.Food);
        StockItem item = new StockItem(product, new BigDecimal("10.00"), 5, LocalDate.now().plusDays(10));
        manager.addStock(item);

        manager.getInventory().clear();
        assertFalse(manager.getInventory().isEmpty());
    }

    @Test
    void findProductByName_ShouldReturnProduct_WhenExists() {
        InventoryManager manager = new InventoryManager();
        Product product = new Product("Banana", Category.Food);
        StockItem item = new StockItem(product, new BigDecimal("5.00"), 10, LocalDate.now().plusDays(5));
        manager.addStock(item);

        Product found = manager.findProductByName("banana");
        assertNotNull(found);
        assertEquals(product, found);
    }

    @Test
    void findProductByName_ShouldReturnNull_WhenNotExists() {
        InventoryManager manager = new InventoryManager();
        assertNull(manager.findProductByName("Nonexistent"));
    }

    @Test
    void getStockForProduct_ShouldReturnEmptyList_WhenNoneExist() {
        InventoryManager manager = new InventoryManager();
        Product product = new Product("Orange", Category.Food);
        List<StockItem> stock = manager.getStockForProduct(product);
        assertNotNull(stock);
        assertTrue(stock.isEmpty());
    }

    @Test
    void getFirstAvailableStockItem_ShouldReturnFirstAvailable() {
        InventoryManager manager = new InventoryManager();
        Product product = new Product("Milk", Category.Food);

        StockItem expired = new StockItem(product, new BigDecimal("2.00"), 5, LocalDate.now().minusDays(1));
        StockItem outOfStock = new StockItem(product, new BigDecimal("2.00"), 0, LocalDate.now().plusDays(5));
        StockItem available = new StockItem(product, new BigDecimal("2.00"), 3, LocalDate.now().plusDays(5));

        manager.addStock(expired);
        manager.addStock(outOfStock);
        manager.addStock(available);

        StockItem result = manager.getFirstAvailableStockItem(product);
        assertEquals(available, result);
    }

    @Test
    void getFirstAvailableStockItem_ShouldReturnNull_WhenNoneAvailable() {
        InventoryManager manager = new InventoryManager();
        Product product = new Product("Juice", Category.Food);

        StockItem expired = new StockItem(product, new BigDecimal("2.00"), 5, LocalDate.now().minusDays(1));
        StockItem outOfStock = new StockItem(product, new BigDecimal("2.00"), 0, LocalDate.now().plusDays(5));

        manager.addStock(expired);
        manager.addStock(outOfStock);

        StockItem result = manager.getFirstAvailableStockItem(product);
        assertNull(result);
    }
}
