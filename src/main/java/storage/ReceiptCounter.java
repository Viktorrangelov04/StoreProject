package storage;

public interface ReceiptCounter {
    int getNextReceiptNumber();
    int getTotalReceiptsIssued();
}
