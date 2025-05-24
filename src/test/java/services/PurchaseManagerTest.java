package services;

import domain.product.Category;
import domain.product.Product;
import domain.product.StockItem;
import domain.receipt.Receipt;
import domain.store.Cashier;
import exceptions.InsufficientFundsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pricing.ExpiryDiscountStrategy;
import storage.ReceiptStorage;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class PurchaseManagerTest {

    private InventoryManager inventoryManager;
    private FinancialManager financialManager;
    private ExpiryDiscountStrategy expiryDiscountStrategy;
    private ReceiptStorage receiptStorage;
    private PurchaseManager purchaseManager;
    private Product banana;
    private StockItem item;

    // Used for verifying saved receipts
    private Receipt lastSavedReceipt;

    @BeforeEach
    void setUp() {
        inventoryManager = new InventoryManager();
        financialManager = new FinancialManager();
        expiryDiscountStrategy = new ExpiryDiscountStrategy(new BigDecimal("10"), 7);

        receiptStorage = new ReceiptStorage() {
            @Override
            public int getNextReceiptNumber() {
                return 1;
            }

            @Override
            public void saveReceipt(Receipt receipt) {
                lastSavedReceipt = receipt; // store for validation
            }

            @Override
            public Receipt loadReceipt(int serialNumber) {
                return lastSavedReceipt;
            }

            @Override
            public List<Receipt> loadAllReceipts() {
                return List.of(lastSavedReceipt);
            }

            @Override
            public int getTotalReceiptsIssued() {
                return lastSavedReceipt != null ? 1 : 0;
            }
        };

        purchaseManager = new PurchaseManager(inventoryManager, receiptStorage, financialManager, expiryDiscountStrategy);

        banana = new Product("Banana", Category.Food);
        item = new StockItem(banana, new BigDecimal("2.00"), 10, LocalDate.now().plusDays(5));
        item.setSellingPrice(new BigDecimal("2.50"));
        inventoryManager.addStock(item);
    }

    @Test
    void addToCart_ShouldAddItemSuccessfully() {
        purchaseManager.addToCart(item, 3);
        assertEquals(1, purchaseManager.getCart().size());
        assertEquals(3, purchaseManager.getCart().get(item));
    }

    @Test
    void addToCart_ShouldThrowForZeroOrNegative() {
        assertThrows(IllegalArgumentException.class, () -> purchaseManager.addToCart(item, 0));
        assertThrows(IllegalArgumentException.class, () -> purchaseManager.addToCart(item, -2));
    }

    @Test
    void validatePayment_ShouldThrowIfInsufficientFunds() {
        purchaseManager.addToCart(item, 2); // total = 5.00

        assertThrows(InsufficientFundsException.class, () -> {
            purchaseManager.processPurchase(
                    fakeScanner("Banana\n2\ndone\n"),
                    new BigDecimal("3.00"),
                    new Cashier("Viktor", BigDecimal.TEN)
            );
        });
    }

    @Test
    void processPurchase_ShouldSucceedAndGenerateReceipt() throws Exception {
        Cashier cashier = new Cashier("Viktor", new BigDecimal("1000"));

        purchaseManager.processPurchase(
                fakeScanner("Banana\n2\ndone\n"),
                new BigDecimal("10.00"),
                cashier
        );

        assertNotNull(lastSavedReceipt);
        assertEquals(1, lastSavedReceipt.getProducts().size());
        assertEquals(new BigDecimal("5.00"), lastSavedReceipt.getTotalPrice()); // 2 x 2.50
        assertEquals(cashier.getName(), lastSavedReceipt.getCashier().getName());
        assertEquals(8, item.getQuantity()); // was 10
    }

    @Test
    void processPurchase_ShouldApplyExpiryDiscount() throws Exception {
        item.setSellingPrice(new BigDecimal("10.00"));
        expiryDiscountStrategy = new ExpiryDiscountStrategy(new BigDecimal("50"), 10); // 50% discount
        purchaseManager = new PurchaseManager(inventoryManager, receiptStorage, financialManager, expiryDiscountStrategy);

        purchaseManager.processPurchase(
                fakeScanner("Banana\n1\ndone\n"),
                new BigDecimal("10.00"),
                new Cashier("Test", BigDecimal.ZERO)
        );

        assertNotNull(lastSavedReceipt);
        assertEquals(new BigDecimal("5.00"), lastSavedReceipt.getTotalPrice()); // Discounted
    }

    // Simulate Scanner input
    private Scanner fakeScanner(String input) {
        return new Scanner(new ByteArrayInputStream(input.getBytes()));
    }
}
