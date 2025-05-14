package Store;
import Products.Product;
import Products.StockItem;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Receipt {
    private static int receiptCounter = 0;
    private final int serialNumber;
    private final Cashier cashier;
    private final LocalDateTime now;
    private final int itemQuantity;
    private final BigDecimal totalPrice;
    private  final Map<StockItem, Integer> products;

    public Receipt(Cashier cashier, Map<StockItem, Integer> products, int itemQuantity){
        this.serialNumber = ++receiptCounter;
        this.cashier = cashier;
        this.now = LocalDateTime.now();
        this.itemQuantity = itemQuantity;
        this.totalPrice = BigDecimal.ZERO;
        this.products = products;
    }

    public int getSerialNumber(){
        return serialNumber;
    }
    public Cashier getCashier(){
        return cashier;
    }
    public LocalDateTime getNow(){
        return now;
    }
    public int getItemQuantity(){
        return itemQuantity;
    }
    public BigDecimal getTotalPrice(){
        return totalPrice;
    }
    public Map<StockItem, Integer> getProducts(){
        return products;
    }

    public String FormatReceipt() {
        StringBuilder sb = new StringBuilder();
        sb.append("Касова бележка № ").append(serialNumber).append("\n");
        sb.append("Касиер: ").append(cashier.getName()).append("\n");
        sb.append("Дата и час: ").append(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n");
        sb.append("Продукти:\n");

        for (Map.Entry<StockItem, Integer> entry : products.entrySet()) {
            sb.append("- ").append(entry.getKey().getProduct().getName())
                    .append(" x").append(entry.getValue())
                    .append("\n");
        }

        sb.append("Обща сума: ").append(totalPrice.setScale(2, RoundingMode.HALF_UP)).append(" лв.\n");

        return sb.toString();
    }

    public void saveToFile() throws IOException {
        String folder = "receipts";
        String filename = "receipt_" + serialNumber + ".txt";

        Path path = Path.of(folder, filename);
        Files.createDirectories(path.getParent());
        Files.write(path, FormatReceipt().getBytes());
    }

}
