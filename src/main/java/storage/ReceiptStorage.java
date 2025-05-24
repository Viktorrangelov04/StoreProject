package storage;

import domain.receipt.Receipt;

import java.util.List;

public interface ReceiptStorage {
    int getNextReceiptNumber();
    void saveReceipt(Receipt receipt);
    Receipt loadReceipt(int serialNumber);
    List<Receipt> loadAllReceipts();
    int getTotalReceiptsIssued();
}
