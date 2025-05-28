package domain.product.integration;

import domain.product.Category;
import domain.product.Product;
import domain.product.StockItem;
import domain.store.Store;
import org.junit.jupiter.api.Test;
import services.InventoryManager;
import services.StoreFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


public class ITStockItemTest {

    private StockItem makeDefaultItem() {
        Product product = new Product("Apple", Category.Food);
        BigDecimal deliveryPrice = new BigDecimal("20");
        int quantity = 10;
        LocalDate expiryDate = LocalDate.now().plusDays(10);
        return new StockItem(product, deliveryPrice, quantity, expiryDate);
    }//Дефаултен предмет, който да се използва във всяко условие, където това не е против условието на опита.

    @Test
    public void getProduct() {
        //Arrange
        Product product = new Product("Apple", Category.Food);
        StockItem item = new StockItem(product, new BigDecimal("20"), 10, LocalDate.now().plusDays(10));
        //Assert
        assertEquals(new Product("Apple", Category.Food), item.getProduct());
    }

    @Test
    public void getDeliveryPrice() {
        //Arrange
        StockItem item = makeDefaultItem();
        //Assert
        assertEquals(new BigDecimal("20"), item.getDeliveryPrice());
    }

    @Test
    public void getSellingPrice() {
        // Arrange
        Store store = StoreFactory.createDefaultStore("Minimart");
        Product banana = new Product("Banana", Category.Food);

        // Act
        store.addStock(banana, 1, new BigDecimal("20"), LocalDate.now().plusDays(10));

        InventoryManager inventoryManager = store.getInventoryManager();
        StockItem item = inventoryManager.getFirstAvailableStockItem(banana);

        // Assert
        BigDecimal expected = new BigDecimal("20")
                .multiply(new BigDecimal("1.2"))
                .setScale(2, RoundingMode.HALF_UP);

        assertEquals(expected, item.getSellingPrice());
    }


    @Test
    public void getQuantity() {
        //Arrange
        StockItem item = makeDefaultItem();
        //Assert
        assertEquals(10, item.getQuantity());
    }

    @Test
    public void getExpiryDate() {
        //Arrange
        StockItem item = makeDefaultItem();
        //Assert
        assertEquals(LocalDate.now().plusDays(10), item.getExpiryDate());
    }

    @Test
    void setQuantity_ShouldUpdateQuantityCorrectly() {
        //Arrange
        StockItem item = makeDefaultItem();
        //Act
        item.setQuantity(42);
        //Assert
        assertEquals(42, item.getQuantity());
    }

    @Test
    public void setDeliveryPrice() {
        //Arrange
        StockItem item = makeDefaultItem();
        //Act
        item.setDeliveryPrice(new BigDecimal("25"));
        //Assert
        assertEquals(new BigDecimal("25"), item.getDeliveryPrice());
    }

    @Test
    public void setSellingPrice() {
        //Arrange
        StockItem item = makeDefaultItem();
        //Act
        item.setSellingPrice(new BigDecimal("25"));
        //Assert
        assertEquals(new BigDecimal("25"), item.getSellingPrice());
    }

    @Test
    void addQuantity_ShouldUpdateQuantityCorrectly() {
        //Arrange
        StockItem item = makeDefaultItem();
        //Act
        item.addQuantity(10);
        //Assert
        assertEquals(20, item.getQuantity());
    }
    @Test
    void addQuantity_ShouldThrow_WhenAmountIsNegative() {
        //Arrange
        StockItem item = makeDefaultItem();
        //Assert
        assertThrows(IllegalArgumentException.class, () -> item.addQuantity(-5));
    }

    @Test
    void removeQuantity_ShouldReduceAmountCorrectly() {
        //Arrange
        StockItem item = makeDefaultItem();
        //Act
        item.removeQuantity(10);
        //Assert
        assertEquals(0, item.getQuantity());
    }

    @Test
    void removeQuantity_ShouldWork_WhenAmountIsExactlyAvailable() {
        //Arrange
        StockItem item = new StockItem(new Product("Apple", Category.Food), new BigDecimal("20"), 5, LocalDate.now().plusDays(10));
        //Act
        item.removeQuantity(5);
        //Assert
        assertEquals(0, item.getQuantity());
    }
    @Test
    void removeQuantity_ShouldThrow_WhenAmountIsNegative() {
        StockItem item = makeDefaultItem();
        assertThrows(IllegalArgumentException.class, () -> item.removeQuantity(-5));
    }

    @Test
    void removeQuantity_ShouldThrow_WhenAmountTooHigh() {
        //Arrange
        StockItem item = makeDefaultItem();
        //Act
        assertThrows(IllegalArgumentException.class, () -> item.removeQuantity(11));
    }

    @Test
    public void isExpired_ShouldReturnTrue_WhenExpired() {
        //Arrange
        StockItem item = new StockItem(new Product("Apple", Category.Food), new BigDecimal("20"), 10, LocalDate.now().minusDays(1));
        //Assert
        assertTrue(item.isExpired());
    }

    @Test
    public void isExpired_ShouldReturnFalse_WhenNotExpired() {
        //Arrange
        StockItem item = makeDefaultItem();
        //Assert
        assertFalse(item.isExpired());
    }

    @Test
    public void isCloseToExpiry_ShouldReturnTrue_WhenCloseToExpiry() {
        //Arrange
        StockItem item = new StockItem(new Product("Apple", Category.Food), new BigDecimal("20"), 10, LocalDate.now().plusDays(5));
        //Assert
        assertTrue(item.isCloseToExpiry(7));
    }

    @Test
    public void isCloseToExpiry_ShouldReturnFalse_WhenNotCloseToExpiry() {
        //Arrange
        StockItem item = makeDefaultItem();
        //Assert
        assertFalse(item.isCloseToExpiry(7));
    }

    private StockItem createItem(int quantity) {
        Product p = new Product("Banana", Category.Food);
        return new StockItem(p, new BigDecimal("10.00"), quantity, LocalDate.now().plusDays(5));
    }

    @Test
    void equals_ShouldReturnTrue_ForSameObject() {
        //Arrange
        StockItem item = createItem(5);
        //Assert
        assertEquals(item, item);
    }

    @Test
    void equals_ShouldReturnTrue_ForSameValues() {
        //Arrange
        StockItem a = createItem(5);
        StockItem b = createItem(5);
        //Assert
        assertEquals(a, b);
    }

    @Test
    void equals_ShouldReturnFalse_ForDifferentQuantity() {
        //Arrange
        StockItem a = createItem(5);
        StockItem b = createItem(6);
        //Assert
        assertNotEquals(a, b);
    }

    @Test
    void equals_ShouldReturnFalse_ForNull() {
        //Arrange
        StockItem a = createItem(5);
        //Assert
        assertNotEquals(null, a);
    }

    @Test
    void equals_ShouldReturnFalse_ForDifferentType() {
        //Arrange
        StockItem a = createItem(5);
        //Assert
        assertNotEquals("not a stock item", a);
    }

    @Test
    void hashCode_ShouldBeEqual_ForEqualObjects() {
        //Arrange
        StockItem a = createItem(5);
        StockItem b = createItem(5);
        //Assert
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void hashCode_ShouldBeConsistent() {
        //Arrange
        StockItem a = createItem(5);
        int hash1 = a.hashCode();
        int hash2 = a.hashCode();
        //Assert
        assertEquals(hash1, hash2);
    }
}