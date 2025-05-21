package Products;

import Store.Store;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class StockItemTest {

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
        assertEquals(product, item.getProduct());
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
        //Arrange
        Store store = new Store("Minimart Seniche", new BigDecimal("20"), new BigDecimal("30"), new BigDecimal("15"), 7);
        StockItem item = makeDefaultItem();
        //Act
        store.addStock(item);

        //Assert
        BigDecimal expected = new BigDecimal("20")
                .multiply(BigDecimal.ONE.add(new BigDecimal("20").divide(new BigDecimal("100"))))
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
    public void applyCloseToExpiryDiscount() {
        //Arrange
        Store store = new Store("Minimart Seniche", new BigDecimal("20"), new BigDecimal("30"), new BigDecimal("15"), 11);
        StockItem item = makeDefaultItem();
        //Act
        store.addStock(item);
        item.applyCloseToExpiryDiscount(store.getExpiryDiscountPercent(), store.getDaysBeforeExpirationThreshold());
        //Assert
        BigDecimal expected = new BigDecimal("20").multiply(BigDecimal.ONE.add(new BigDecimal("20").divide(new BigDecimal("100"))))
                .multiply(BigDecimal.ONE.subtract(new BigDecimal("15").divide(new BigDecimal("100"))))
                .setScale(2, RoundingMode.HALF_UP);
        assertEquals(expected, item.getSellingPrice());
    }

    @Test
    void applyCloseToExpiryDiscount_ShouldNotChangePrice_WhenNotCloseToExpiry() {
        //Arrange
        Store store = new Store("Minimart Seniche", new BigDecimal("20"), new BigDecimal("30"), new BigDecimal("15"), 7);
        StockItem item = makeDefaultItem();//10>7 days
        store.addStock(item);
        //Act
        item.applyCloseToExpiryDiscount(store.getExpiryDiscountPercent(), store.getDaysBeforeExpirationThreshold());
        BigDecimal expected = new BigDecimal("20") // delivery price
                .multiply(BigDecimal.ONE.add(new BigDecimal("20").divide(new BigDecimal("100")))) // 20% markup
                .setScale(2, RoundingMode.HALF_UP);
        //Assert
        assertEquals(expected, item.getSellingPrice());
    }

    @Test
    void addQuantity() {
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
    void removeQuantity() {
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
}