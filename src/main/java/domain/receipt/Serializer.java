package domain.receipt;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class Serializer {

    public static void saveToTextFile(Receipt receipt) throws IOException {
        String folder = "receipts";
        String filename = "receipt_" + receipt.getSerialNumber() + ".txt";

        Path path = Path.of(folder, filename);
        Files.createDirectories(path.getParent());
        Files.write(path, receipt.formatReceipt().getBytes());
    }

    public static void serialize(Receipt receipt) throws IOException {
        String folder = "receipts";
        String filename = "receipt_" + receipt.getSerialNumber() + ".ser";

        Path path = Path.of(folder, filename);
        Files.createDirectories(path.getParent());

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
            oos.writeObject(receipt);
        }
    }

    public static Receipt deserialize(int serialNumber) throws IOException, ClassNotFoundException {
        String path = "receipts/receipt_" + serialNumber + ".ser";

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
            return (Receipt) ois.readObject();
        }
    }
}
