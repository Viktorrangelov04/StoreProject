package services;

import domain.product.Category;
import domain.product.Product;
import domain.product.StockItem;
import domain.receipt.Receipt;
import domain.store.Cashier;
import exceptions.InsufficientFundsException;
import org.junit.jupiter.api.Test;
import pricing.ExpiryDiscountStrategy;
import storage.ReceiptStorage;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class PurchaseManagerTest {

    @Test
    void addToCart() {
        InventoryManager inventoryManager = new InventoryManager();
        FinancialManager financialManager = new FinancialManager();
        ExpiryDiscountStrategy discountStrategy = new ExpiryDiscountStrategy(new BigDecimal("10"), 7);
        PurchaseManager purchaseManager = new PurchaseManager(inventoryManager, dummyReceiptStorage(), financialManager, discountStrategy);

        Product banana = new Product("Banana", Category.Food);
        StockItem item = new StockItem(banana, new BigDecimal("2.00"), 10, LocalDate.now().plusDays(5));
        item.setSellingPrice(new BigDecimal("2.50"));
        inventoryManager.addStock(item);

        purchaseManager.addToCart(item, 3);

        assertEquals(1, purchaseManager.getCart().size());
        assertEquals(3, purchaseManager.getCart().get(item));
    }

    @Test
    void addToCart_ShouldThrowForZeroOrNegative() {
        InventoryManager inventoryManager = new InventoryManager();
        FinancialManager financialManager = new FinancialManager();
        ExpiryDiscountStrategy discountStrategy = new ExpiryDiscountStrategy(new BigDecimal("10"), 7);
        PurchaseManager purchaseManager = new PurchaseManager(inventoryManager, dummyReceiptStorage(), financialManager, discountStrategy);

        Product banana = new Product("Banana", Category.Food);
        StockItem item = new StockItem(banana, new BigDecimal("2.00"), 10, LocalDate.now().plusDays(5));
        item.setSellingPrice(new BigDecimal("2.50"));

        assertThrows(IllegalArgumentException.class, () -> purchaseManager.addToCart(item, 0));
        assertThrows(IllegalArgumentException.class, () -> purchaseManager.addToCart(item, -1));
    }

    @Test
    void validatePayment() {
        InventoryManager inventoryManager = new InventoryManager();
        FinancialManager financialManager = new FinancialManager();
        ExpiryDiscountStrategy discountStrategy = new ExpiryDiscountStrategy(new BigDecimal("10"), 7);
        ReceiptStorage storage = dummyReceiptStorage();
        PurchaseManager purchaseManager = new PurchaseManager(inventoryManager, storage, financialManager, discountStrategy);

        Product banana = new Product("Banana", Category.Food);
        StockItem item = new StockItem(banana, new BigDecimal("2.00"), 10, LocalDate.now().plusDays(5));
        item.setSellingPrice(new BigDecimal("2.50"));
        inventoryManager.addStock(item);

        purchaseManager.addToCart(item, 2);

        Cashier cashier = new Cashier("Viktor", BigDecimal.TEN);
        assertThrows(InsufficientFundsException.class, () ->
                purchaseManager.processPurchase(fakeScanner("Banana\n2\ndone\n"), new BigDecimal("3.00"), cashier));
    }

    @Test
    void processPurchase() throws Exception {
        InventoryManager inventoryManager = new InventoryManager();
        FinancialManager financialManager = new FinancialManager();
        ExpiryDiscountStrategy discountStrategy = new ExpiryDiscountStrategy(new BigDecimal("10"), 7);
        ReceiptHolder holder = new ReceiptHolder();

        PurchaseManager purchaseManager = new PurchaseManager(inventoryManager, holder, financialManager, discountStrategy);

        Product banana = new Product("Banana", Category.Food);
        StockItem item = new StockItem(banana, new BigDecimal("2.00"), 10, LocalDate.now().plusDays(10)); // No discount
        item.setSellingPrice(new BigDecimal("2.50"));
        inventoryManager.addStock(item);

        Cashier cashier = new Cashier("Viktor", new BigDecimal("1200"));

        purchaseManager.processPurchase(fakeScanner("Banana\n2\ndone\n"), new BigDecimal("10.00"), cashier);

        Receipt r = holder.last;
        assertNotNull(r);
        assertEquals(1, r.getProducts().size());
        assertEquals(new BigDecimal("5.00"), r.getTotalPrice());
        assertEquals("Viktor", r.getCashier());
    }

    @Test
    void processPurchase_ShouldApplyExpiryDiscount() throws Exception {
        InventoryManager inventoryManager = new InventoryManager();
        FinancialManager financialManager = new FinancialManager();
        ExpiryDiscountStrategy discountStrategy = new ExpiryDiscountStrategy(new BigDecimal("50"), 10);
        ReceiptHolder holder = new ReceiptHolder();

        PurchaseManager purchaseManager = new PurchaseManager(inventoryManager, holder, financialManager, discountStrategy);

        Product banana = new Product("Banana", Category.Food);
        StockItem item = new StockItem(banana, new BigDecimal("2.00"), 1, LocalDate.now().plusDays(5));
        item.setSellingPrice(new BigDecimal("10.00"));
        inventoryManager.addStock(item);

        purchaseManager.processPurchase(fakeScanner("Banana\n1\ndone\n"), new BigDecimal("10.00"), new Cashier("Test", BigDecimal.ZERO));

        Receipt r = holder.last;
        assertNotNull(r);
        assertEquals(new BigDecimal("5.00"), r.getTotalPrice());
    }

    private Scanner fakeScanner(String input) {
        return new Scanner(new ByteArrayInputStream(input.getBytes()));
    }

    private ReceiptStorage dummyReceiptStorage() {
        return new ReceiptStorage() {
            public int getNextReceiptNumber() { return 1; }
            public void saveReceipt(Receipt r) {}
            public Receipt loadReceipt(int n) { return null; }
            public List<Receipt> loadAllReceipts() { return List.of(); }
            public int getTotalReceiptsIssued() { return 0; }
        };
    }

    static class ReceiptHolder implements ReceiptStorage {
        public Receipt last;
        public int getNextReceiptNumber() { return 1; }
        public void saveReceipt(Receipt receipt) { this.last = receipt; }
        public Receipt loadReceipt(int serialNumber) { return last; }
        public List<Receipt> loadAllReceipts() { return List.of(last); }
        public int getTotalReceiptsIssued() { return last != null ? 1 : 0; }
    }
}
