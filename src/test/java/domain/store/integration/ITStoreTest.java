package domain.store.integration;

import domain.product.Category;
import domain.product.Product;
import domain.product.StockItem;
import domain.store.Cashier;
import domain.store.Store;
import exceptions.InsufficientFundsException;
import exceptions.InsufficientStockException;
import org.junit.jupiter.api.Test;
import services.StoreFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class ITStoreTest {
    @Test
    void getId() {
        //Arrange
        Store store = StoreFactory.createDefaultStore("Minimart");

        //Assert
        assertNotNull(store.getId());
    }

    @Test
    void getStoreName() {
        //Arrange
        Store store = StoreFactory.createDefaultStore("Minimart");
        //Assert
        assertEquals("Minimart", store.getStoreName());
    }

    @Test
    void setStoreName() {
        //Arrange
        Store store = StoreFactory.createDefaultStore("Minimart");
        //Act
        store.setStoreName("New minmart");
        //Assert
        assertEquals("New minmart", store.getStoreName());
    }

    @Test
    void addStock_ShouldAddStockCorrectly() {
        //Arrange
        Store store = StoreFactory.createDefaultStore("Test Store");
        Product apple = new Product("Apple", Category.Food);

        store.addStock(apple, 5, new BigDecimal("2.00"), LocalDate.now().plusDays(10));
        //Act
        StockItem item = store.getStockForProduct(apple).getFirst();
        BigDecimal expectedPrice = new BigDecimal("2.00")
                .multiply(BigDecimal.ONE.add(new BigDecimal("20").divide(new BigDecimal("100"))))
                .setScale(2, RoundingMode.HALF_UP);

        //Assert
        assertEquals(expectedPrice, item.getSellingPrice());
        assertEquals(new BigDecimal("10.00"), store.getTotalDeliveryCost());
    }

    @Test
    void getProfit() {
        //Arrange
        Store store = StoreFactory.createDefaultStore("Test Store");
        store.addCashier("Viktor", new BigDecimal("1200"));

        Product apple = new Product("Apple", Category.Food);
        store.addStock(apple, 2, new BigDecimal("3.00"), LocalDate.now().plusDays(10)); // 6.00 delivery cost

        store.getFinancialManager().addRevenue(new BigDecimal("20.00"));

        //Assert
        assertEquals(new BigDecimal("-1186.00"), store.getProfit());
    }

    @Test
    void processPurchase_ShouldProcessPurchase() throws InsufficientStockException, InsufficientFundsException, IOException {
        //Arrange
        Store store = StoreFactory.createDefaultStore("Test Store");
        Product milk = new Product("Milk", Category.Food);
        store.addStock(milk, 1, new BigDecimal("2.00"), LocalDate.now().plusDays(5)); // eligible for discount

        Cashier cashier = store.addCashier("Ivan", new BigDecimal("1000"));

        //Act
        Scanner scanner = new Scanner("Milk\n1\ndone\n");
        store.processPurchase(scanner, new BigDecimal("10.00"), cashier);

        StockItem item = store.getStockForProduct(milk).getFirst();

        //Assert
        assertEquals(0, item.getQuantity());

        BigDecimal price = new BigDecimal("2.00")
                .multiply(BigDecimal.ONE.add(new BigDecimal("20").divide(new BigDecimal("100")))) // markup
                .multiply(BigDecimal.ONE.subtract(new BigDecimal("15").divide(new BigDecimal("100")))) // discount
                .setScale(2, RoundingMode.HALF_UP);

        assertEquals(price, item.getSellingPrice());
        assertEquals(price, store.getStoreRevenue());
    }
}