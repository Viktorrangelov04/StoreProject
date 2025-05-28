package domain.receipt;

import domain.product.StockItem;
import domain.store.Cashier;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class Receipt implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final int serialNumber;
    private final LocalDateTime timestamp;
    private final int itemQuantity;
    private final BigDecimal totalPrice;
    private final Map<StockItem, Integer> products;
    private final String cashierName;
    public static final int RECEIPT_WIDTH = 30;

    public Receipt(int serialNumber, Cashier cashier, Map<StockItem, Integer> products, int itemQuantity, BigDecimal totalPrice) {
        this.serialNumber = serialNumber;
        this.cashierName = cashier.getName();
        this.timestamp = LocalDateTime.now();
        this.itemQuantity = itemQuantity;
        this.totalPrice = totalPrice;
        this.products = products;
    }

    public Receipt(int serialNumber, Cashier cashier, Map<StockItem, Integer> products, int itemQuantity, BigDecimal totalPrice, LocalDateTime timestamp) {
        this.serialNumber = serialNumber;
        this.cashierName = cashier.getName();
        this.timestamp = timestamp;
        this.itemQuantity = itemQuantity;
        this.totalPrice = totalPrice;
        this.products = products;
    }

    public int getSerialNumber() { return serialNumber; }
    public String getCashier() { return cashierName; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public int getItemQuantity() { return itemQuantity; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public Map<StockItem, Integer> getProducts() { return products; }

    public String center(String text, int width) {
        if (text.length() >= width) {
            return text;
        }
        int totalPadding = width - text.length();
        int leftPadding = totalPadding / 2;
        int rightPadding = totalPadding - leftPadding;
        return " ".repeat(leftPadding) + text + " ".repeat(rightPadding);
    }

    public String formatReceipt() {
        StringBuilder sb = new StringBuilder();

        sb.append(center("Фалит ООД", RECEIPT_WIDTH)).append("\n");
        sb.append(center("ЕИК: 100000000", RECEIPT_WIDTH)).append("\n");
        sb.append(center("Касова бележка № " + serialNumber, RECEIPT_WIDTH)).append("\n");
        sb.append(center("Касиер: " + cashierName, RECEIPT_WIDTH)).append("\n");
        sb.append(center("Дата и час: " + timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), RECEIPT_WIDTH)).append("\n");
        sb.append(center("Продукти:", RECEIPT_WIDTH)).append("\n");

        for (Map.Entry<StockItem, Integer> entry : products.entrySet()) {
            String productLine = "- " + entry.getKey().getProduct().getName()
                    + " x" + entry.getValue()
                    + " @ " + entry.getKey().getSellingPrice().setScale(2, RoundingMode.HALF_UP)
                    + " лв";
            sb.append(center(productLine, RECEIPT_WIDTH)).append("\n");
        }

        sb.append(center("Обща сума: " + totalPrice.setScale(2, RoundingMode.HALF_UP) + " лв.", RECEIPT_WIDTH)).append("\n");

        return sb.toString();
    }
}
