package storage.unit;

import org.junit.jupiter.api.Test;
import storage.FileReceiptStorage;

import static org.junit.jupiter.api.Assertions.*;

class FileReceiptStorageTest {

    @Test
    void getNextReceiptNumber_ShouldIncrementCounter() {
        FileReceiptStorage storage = new FileReceiptStorage();
        int first = storage.getNextReceiptNumber();
        int second = storage.getNextReceiptNumber();
        assertEquals(first + 1, second);
    }

    @Test
    void getTotalReceiptsIssued_ShouldReturnLastReceiptNumber() {
        FileReceiptStorage storage = new FileReceiptStorage();
        int current = storage.getTotalReceiptsIssued();
        storage.getNextReceiptNumber();
        assertEquals(current + 1, storage.getTotalReceiptsIssued());
    }
}
