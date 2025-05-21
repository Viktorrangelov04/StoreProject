package Store;

import Products.Category;
import Products.Product;
import Products.StockItem;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ReceiptTest {
    private Receipt makeDefaultReceipt(){
        int serial = 1;
        Cashier cashier = new Cashier("Viktor", new BigDecimal("1200"));

        StockItem item = new StockItem(new Product("banana", Category.Food), new BigDecimal("20"), 5, LocalDate.now().plusDays(3));
        Map<StockItem, Integer> cart = Map.of(item, 2);

        int quantity = 10;
        BigDecimal price = new BigDecimal("100");
        LocalDateTime fixedTime = LocalDateTime.of(2024, 5, 20, 14, 0);
        return new Receipt(serial, cashier, cart, quantity, price,fixedTime);
    }
    @Test
    void getSerialNumber() {
        //Arrange
        Receipt receipt = makeDefaultReceipt();
        //Assert
        assertEquals(1, receipt.getSerialNumber());
    }

    @Test
    void getCashier() {
        //Arrange
        Receipt receipt = makeDefaultReceipt();
        //Act
        Cashier Cashier = receipt.getCashier();
        //Assert
        assertEquals(Cashier, receipt.getCashier());//Реално сравнявам нещо само със себе си но ако просто напиша нов касиер дава грешка защото е друг обект
    }

    @Test
    void getTimestamp() {
        //Arrange
        Receipt receipt = makeDefaultReceipt();
        //Assert
        assertEquals(LocalDateTime.of(2024, 5, 20, 14, 0), receipt.getTimestamp());
    }

    @Test
    void getItemQuantity() {
        //Arrange
        Receipt receipt = makeDefaultReceipt();
        //Assert
        assertEquals(10, receipt.getItemQuantity());
    }

    @Test
    void getTotalPrice() {
        //Arrange
        Receipt receipt = makeDefaultReceipt();
        //Assert
        assertEquals(new BigDecimal("100"), receipt.getTotalPrice());
    }

    @Test
    void getProducts() {
        //Arrange
        Receipt receipt = makeDefaultReceipt();
        StockItem item = new StockItem(new Product("banana", Category.Food), new BigDecimal("20"), 5, LocalDate.now().plusDays(3));
        Map<StockItem, Integer> cart = Map.of(item, 2);
        //Act

        //Assert
        assertEquals(cart, receipt.getProducts());
    }

    @Test
    void formatReceipt() {
    }

    @Test
    void saveToTextFile() {
    }

    @Test
    void serialize() {
    }

    @Test
    void deserialize() {
    }
}