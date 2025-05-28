package storage;

import domain.product.Category;
import domain.product.Product;
import domain.product.StockItem;
import domain.receipt.Receipt;
import domain.store.Cashier;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ITFileReceiptStorageTest {

    private FileReceiptStorage storage;

    @BeforeEach
    void setUp() throws IOException {
        storage = new FileReceiptStorage();
        clearReceipts();
    }

    @AfterEach
    void tearDown() throws IOException {
        clearReceipts();
    }

    @Test
    void getNextReceiptNumber_ShouldReturnNextReceiptNumber() {
        //Arrange
        int first = storage.getNextReceiptNumber();
        int second = storage.getNextReceiptNumber();

        //Assert
        assertEquals(first + 1, second);
    }

    @Test
    void saveAndLoadReceipt_ShouldSaveReceipt_AndReturnReceipt() {
        // Arrange
        int serial = storage.getNextReceiptNumber();
        Cashier cashier = new Cashier("Viktor", new BigDecimal("1200"));

        Product banana = new Product("Banana", Category.Food);
        StockItem item = new StockItem(banana, new BigDecimal("2.00"), 5, LocalDateTime.now().toLocalDate().plusDays(3));
        item.setSellingPrice(new BigDecimal("3.00"));

        Receipt receipt = new Receipt(serial, cashier, Map.of(item, 2), 2, new BigDecimal("6.00"));

        // Act
        storage.saveReceipt(receipt);
        Receipt loaded = storage.loadReceipt(serial);

        // Assert
        assertNotNull(loaded);
        assertEquals(serial, loaded.getSerialNumber());
        assertEquals("Viktor", loaded.getCashier());
        assertEquals(new BigDecimal("6.00"), loaded.getTotalPrice());
        assertEquals(2, loaded.getItemQuantity());
    }

    @Test
    void loadAllReceipts_ShouldLoadAllReceipts(){
        //Arrange
        int serial1 = storage.getNextReceiptNumber();
        int serial2 = storage.getNextReceiptNumber();
        Cashier cashier = new Cashier("Mira", new BigDecimal("1000"));

        Product apple = new Product("Apple", Category.Food);
        StockItem item = new StockItem(apple, new BigDecimal("1.50"), 3, LocalDateTime.now().toLocalDate().plusDays(5));
        item.setSellingPrice(new BigDecimal("2.00"));

        //Act
        storage.saveReceipt(new Receipt(serial1, cashier, Map.of(item, 1), 1, new BigDecimal("2.00")));
        storage.saveReceipt(new Receipt(serial2, cashier, Map.of(item, 2), 2, new BigDecimal("4.00")));

        List<Receipt> receipts = storage.loadAllReceipts();

        //Assert
        assertEquals(2, receipts.size());
    }

    @Test
    void getTotalReceiptsIssued_ShouldReturnReceiptCount() {
        //Arrange
        int before = storage.getTotalReceiptsIssued();
        storage.getNextReceiptNumber();
        int after = storage.getTotalReceiptsIssued();

        //Assert
        assertEquals(before + 1, after);
    }

    private void clearReceipts() throws IOException {
        Path folder = Paths.get("receipts");
        if (Files.exists(folder)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder, "receipt_*")) {
                for (Path path : stream) {
                    Files.deleteIfExists(path);
                }
            }
        }
        Files.writeString(Paths.get("receipts/receipt_counter.txt"), "0");
    }
}
