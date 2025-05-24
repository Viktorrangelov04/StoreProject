package storage;

import domain.receipt.Receipt;

public interface ReceiptWriter {
    void saveReceipt(Receipt receipt);
}
