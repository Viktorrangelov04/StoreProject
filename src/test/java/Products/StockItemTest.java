package Products;

import Store.Store;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class StockItemTest {

    @Test
    public void getProduct() {
        //Arrange
        Product product = new Product("Apple", Category.Food);
        LocalDate expiryDate = LocalDate.of(2026, 1, 1);
        StockItem item = new StockItem(product, new BigDecimal("20"), 10, expiryDate);
        //Act
        Product product2 = item.getProduct();
        //Assert
        assertEquals(product, product2);
    }

    @Test
    public void getDeliveryPrice() {
        //Arrange
        Product product = new Product("Apple", Category.Food);
        LocalDate expiryDate = LocalDate.of(2026, 1, 1);
        StockItem item = new StockItem(product, new BigDecimal("20"), 10, expiryDate);
        //Act
        BigDecimal deliveryPrice = item.getDeliveryPrice();
        //Assert
        assertEquals(new BigDecimal("20"), deliveryPrice);
    }

    @Test
    public void getSellingPrice() {
        //Arrange
        Store store = new Store("Minimart Seniche", new BigDecimal("20"), new BigDecimal("30"), new BigDecimal("15"), 7);
        Product product = new Product("Apple", Category.Food);
        LocalDate expiryDate = LocalDate.of(2026, 1, 1);
        StockItem item = new StockItem(product, new BigDecimal("20"), 10, expiryDate);
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
        Product product = new Product("Apple", Category.Food);
        LocalDate expiryDate = LocalDate.of(2026, 1, 1);
        StockItem item = new StockItem(product, new BigDecimal("20"), 10, expiryDate);
        //Act
        int quantity = item.getQuantity();
        //Assert
        assertEquals(10, quantity);
    }

    @Test
    public void getExpiryDate() {
        //Arrange
        Product product = new Product("Apple", Category.Food);
        LocalDate expiryDate = LocalDate.of(2026, 1, 1);
        StockItem item = new StockItem(product, new BigDecimal("20"), 10, expiryDate);
        //Act
        LocalDate expected= item.getExpiryDate();
        //Assert
        assertEquals(expiryDate, expected);
    }

    @Test
    public void setDeliveryPrice() {
        //Arrange
        Product product = new Product("Apple", Category.Food);
        LocalDate expiryDate = LocalDate.of(2026, 1, 1);
        StockItem item = new StockItem(product, new BigDecimal("20"), 10, expiryDate);
        //Act
        item.setDeliveryPrice(new BigDecimal("25"));
        //Assert
        assertEquals(new BigDecimal("25"), item.getDeliveryPrice());
    }

    @Test
    public void setSellingPrice() {
        //Arrange
        Product product = new Product("Apple", Category.Food);
        LocalDate expiryDate = LocalDate.of(2026, 1, 1);
        StockItem item = new StockItem(product, new BigDecimal("20"), 10, expiryDate);
        //Act
        item.setSellingPrice(new BigDecimal("25"));
        assertEquals(new BigDecimal("25"), item.getSellingPrice());
    }

    @Test
    public void applyCloseToExpiryDiscount() {
        //Arrange
        Store store = new Store("Minimart Seniche", new BigDecimal("20"), new BigDecimal("30"), new BigDecimal("15"), 7);
        Product product = new Product("Apple", Category.Food);
        LocalDate expiryDate = LocalDate.of(2026, 1, 1);
        StockItem item = new StockItem(product, new BigDecimal("20"), 10, expiryDate);
        //Act
        store.addStock(item);
        BigDecimal discount=store.getExpiryDiscountPercent();
        item.applyCloseToExpiryDiscount(discount);

        //Assert
        BigDecimal expected = new BigDecimal("20").multiply(BigDecimal.ONE.add(new BigDecimal("20").divide(new BigDecimal("100"))))
                .multiply(BigDecimal.ONE.subtract(new BigDecimal("15").divide(new BigDecimal("100"))))
                .setScale(2, RoundingMode.HALF_UP);
        assertEquals(expected, item.getSellingPrice());
    }

    @Test
    void addQuantity() {
        //Arrange
        Product product = new Product("Apple", Category.Food);
        LocalDate expiryDate = LocalDate.of(2026, 1, 1);
        StockItem item = new StockItem(product, new BigDecimal("20"), 10, expiryDate);
        //Act
        item.addQuantity(10);
        //Assert
        assertEquals(20, item.getQuantity());
    }

    @Test
    void removeQuantity() {
        //Arrange
        Product product = new Product("Apple", Category.Food);
        LocalDate expiryDate = LocalDate.of(2026, 1, 1);
        StockItem item = new StockItem(product, new BigDecimal("20"), 10, expiryDate);
        //Act
        item.removeQuantity(10);
        //Assert
        assertEquals(0, item.getQuantity());

    }

    @Test
    public void isExpired() {
        //Arrange
        Product product = new Product("Apple", Category.Food);
        LocalDate expiryDate = LocalDate.of(2025, 1, 1);
        StockItem item = new StockItem(product, new BigDecimal("20"), 10, expiryDate);
        //Assert
        assertTrue(item.isExpired());
    }

    @Test
    public void isCloseToExpiry() {
    }


}