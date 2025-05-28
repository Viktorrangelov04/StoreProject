package services.integration;

import domain.product.Category;
import domain.product.Product;
import domain.product.StockItem;
import org.junit.jupiter.api.Test;
import services.InventoryManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ITInventoryManagerTest {

    @Test
    void addStock_ShouldAddToInventory() {
        InventoryManager manager = new InventoryManager();
        Product product = new Product("Milk", Category.Food);
        StockItem item = new StockItem(product, new BigDecimal("1.00"), 5, LocalDate.now().plusDays(5));

        manager.addStock(item);

        Map<Product, List<StockItem>> inventory = manager.getInventory();
        assertTrue(inventory.containsKey(product));
        assertEquals(1, inventory.get(product).size());
        assertEquals(item, inventory.get(product).getFirst());
    }

    @Test
    void findProductByName_ShouldReturnProduct() {
        InventoryManager manager = new InventoryManager();
        Product product = new Product("Bread", Category.Food);
        StockItem item = new StockItem(product, new BigDecimal("0.80"), 10, LocalDate.now().plusDays(3));
        manager.addStock(item);

        Product found = manager.findProductByName("bread");
        assertNotNull(found);
        assertEquals("Bread", found.getName());
    }

    @Test
    void findProductByName_ShouldReturnNull_WhenNotFound() {
        InventoryManager manager = new InventoryManager();
        assertNull(manager.findProductByName("Unknown"));
    }

    @Test
    void getStockForProduct_ShouldReturnStock() {
        InventoryManager manager = new InventoryManager();
        Product product = new Product("Eggs", Category.Food);
        StockItem item1 = new StockItem(product, new BigDecimal("0.20"), 12, LocalDate.now().plusDays(7));
        StockItem item2 = new StockItem(product, new BigDecimal("0.25"), 6, LocalDate.now().plusDays(6));
        manager.addStock(item1);
        manager.addStock(item2);

        List<StockItem> stock = manager.getStockForProduct(product);
        assertEquals(2, stock.size());
    }

    @Test
    void getStockForProduct_ShouldReturnEmptyList_WhenNoneExist() {
        InventoryManager manager = new InventoryManager();
        Product product = new Product("Cheese", Category.Food);

        List<StockItem> stock = manager.getStockForProduct(product);
        assertNotNull(stock);
        assertTrue(stock.isEmpty());
    }

    @Test
    void getFirstAvailableStockItem_ShouldReturnFirstAvailableStockItem_InInventory() {
        InventoryManager manager = new InventoryManager();
        Product product = new Product("Yogurt", Category.Food);
        StockItem available = new StockItem(product, new BigDecimal("1.00"), 3, LocalDate.now().plusDays(3));
        StockItem expired = new StockItem(product, new BigDecimal("1.00"), 10, LocalDate.now().minusDays(1));
        StockItem zeroQty = new StockItem(product, new BigDecimal("1.00"), 0, LocalDate.now().plusDays(5));

        manager.addStock(expired);
        manager.addStock(zeroQty);
        manager.addStock(available);

        StockItem found = manager.getFirstAvailableStockItem(product);
        assertEquals(available, found);
    }

    @Test
    void getFirstAvailableStockItem_ShouldReturnNull_WhenNoValidItem() {
        InventoryManager manager = new InventoryManager();
        Product product = new Product("Butter", Category.Food);
        StockItem expired = new StockItem(product, new BigDecimal("1.00"), 2, LocalDate.now().minusDays(2));
        StockItem zeroQty = new StockItem(product, new BigDecimal("1.00"), 0, LocalDate.now().plusDays(5));
        manager.addStock(expired);
        manager.addStock(zeroQty);

        assertNull(manager.getFirstAvailableStockItem(product));
    }
}
