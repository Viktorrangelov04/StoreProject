package Store;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class FileReceiptStorage implements ReceiptStorage {
    private static final String COUNTER_FILE = "receipts/receipt_counter.txt";
    private static final String RECEIPT_FOLDER = "receipts";
    private int lastReceiptNumber = 0;

    public FileReceiptStorage() {
        loadCounter();
    }

    private void loadCounter() {
        try {
            Path path = Path.of(COUNTER_FILE);
            if (Files.exists(path)) {
                lastReceiptNumber = Integer.parseInt(Files.readString(path).trim());
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Could not load receipt counter. Defaulting to 0.");
            lastReceiptNumber = 0;
        }
    }

    private void saveCounter() {
        try {
            Files.createDirectories(Path.of(RECEIPT_FOLDER));
            Files.writeString(Path.of(COUNTER_FILE), String.valueOf(lastReceiptNumber));
        } catch (IOException e) {
            System.out.println("Failed to save receipt counter: " + e.getMessage());
        }
    }

    @Override
    public int getNextReceiptNumber() {
        lastReceiptNumber++;
        saveCounter();
        return lastReceiptNumber;
    }

    @Override
    public void saveReceipt(Receipt receipt) {
        try {
            receipt.saveToTextFile();
            receipt.serialize();
        } catch (IOException e) {
            System.out.println("Failed to save receipt: " + e.getMessage());
        }
    }

    @Override
    public Receipt loadReceipt(int serialNumber) {
        try {
            return Receipt.deserialize(serialNumber);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Failed to load receipt #" + serialNumber + ": " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Receipt> loadAllReceipts() {
        List<Receipt> receipts = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Path.of(RECEIPT_FOLDER), "receipt_*.ser")) {
            for (Path path : stream) {
                String filename = path.getFileName().toString();
                String numberPart = filename.replace("receipt_", "").replace(".ser", "");
                try {
                    int serial = Integer.parseInt(numberPart);
                    Receipt r = loadReceipt(serial);
                    if (r != null) {
                        receipts.add(r);
                    }
                } catch (NumberFormatException ignored) {}
            }
        } catch (IOException e) {
            System.out.println("Error reading receipts folder: " + e.getMessage());
        }
        return receipts;
    }

    @Override
    public int getTotalReceiptsIssued() {
        return lastReceiptNumber;
    }
}
