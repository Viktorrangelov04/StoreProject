package domain.receipt.integration;

import domain.receipt.Receipt;
import domain.receipt.Serializer;
import domain.store.Cashier;
import domain.product.Category;
import domain.product.Product;
import domain.product.StockItem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ITReceiptTest {
    private Receipt makeDefaultReceipt(){
        int serial = 1;
        Cashier cashier = new Cashier("Viktor", new BigDecimal("1200"));

        StockItem item = new StockItem(new Product("banana", Category.Food), new BigDecimal("20"), 5, LocalDate.now().plusDays(3));
        item.setSellingPrice(new BigDecimal("50"));
        Map<StockItem, Integer> cart = Map.of(item, 2);

        int quantity = 2;
        BigDecimal price = new BigDecimal("100");
        LocalDateTime fixedTime = LocalDateTime.of(2025, 5, 22, 0, 0);
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
        //Assert
        assertEquals("Viktor", receipt.getCashier());
    }

    @Test
    void getTimestamp() {
        //Arrange
        Receipt receipt = makeDefaultReceipt();
        //Assert
        assertEquals(LocalDateTime.of(2025, 5, 22, 0, 0), receipt.getTimestamp());
    }

    @Test
    void getItemQuantity() {
        //Arrange
        Receipt receipt = makeDefaultReceipt();
        //Assert
        assertEquals(2, receipt.getItemQuantity());
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
    void formatReceipt_ShouldFormatCorrectly() {
        // Arrange
        Receipt receipt = makeDefaultReceipt();

        String[] lines = {
                "Фалит ООД",
                "ЕИК: 100000000",
                "Касова бележка № 1",
                "Касиер: Viktor",
                "Дата и час: 2025-05-22 00:00:00",
                "Продукти:",
                "- banana x2 @ 50.00 лв",
                "Обща сума: 100.00 лв."
        };

        String expected = Arrays.stream(lines)
                .map(line -> receipt.center(line, Receipt.RECEIPT_WIDTH))
                .collect(Collectors.joining("\n")) + "\n";

        // Act
        String actual = receipt.formatReceipt();

        // Assert
        assertEquals(expected, actual);
    }

    private static final String RECEIPT_FOLDER = "receipts";
    private static final String RECEIPT_FILENAME = "receipt_1.txt";

    @AfterEach
    void cleanup() throws IOException {
        Path path = Path.of(RECEIPT_FOLDER, RECEIPT_FILENAME);
        if (Files.exists(path)) {
            Files.delete(path);
        }
    }

    @Test
    void saveToTextFile_ShouldCreateFileWithExpectedContent() throws IOException {
        Receipt receipt = makeDefaultReceipt();
        Serializer.saveToTextFile(receipt);

        Path filePath = Path.of(RECEIPT_FOLDER, RECEIPT_FILENAME);
        assertTrue(Files.exists(filePath));

        String content = Files.readString(filePath);
        assertTrue(content.contains("Касова бележка"));
        assertTrue(content.contains("Viktor"));
        assertTrue(content.contains("banana"));
        assertTrue(content.contains("50.00"));
        assertTrue(content.contains("100.00"));
        assertTrue(content.contains("2025-05-22 00:00:00"));
    }

    @Test
    void serialize_deserialize_ShouldBeAbleToSerialize_AndDeserialize() throws IOException, ClassNotFoundException {
        // Arrange
        Receipt original = makeDefaultReceipt();
        Serializer.serialize(original);

        // Act
        Receipt deserialized = Serializer.deserialize(original.getSerialNumber());

        // Assert
        assertEquals(original.getSerialNumber(), deserialized.getSerialNumber());
        assertEquals(original.getCashier(), deserialized.getCashier());
        assertEquals(original.getTimestamp(), deserialized.getTimestamp());
        assertEquals(original.getItemQuantity(), deserialized.getItemQuantity());
        assertEquals(original.getTotalPrice(), deserialized.getTotalPrice());
        assertEquals(original.getProducts(), deserialized.getProducts());
    }

}