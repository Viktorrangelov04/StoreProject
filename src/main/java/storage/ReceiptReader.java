package storage;

import domain.receipt.Receipt;

import java.util.List;

public interface ReceiptReader {
    Receipt loadReceipt(int serialNumber);
    List<Receipt> loadAllReceipts();
}
