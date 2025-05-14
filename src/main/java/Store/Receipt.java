package Store;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Receipt {
    private static int receiptCounter = 0;
    private final int serialNumber;
    private final Cashier cashier;
    private final LocalDateTime now;
    private final int itemQuantity;
    private final BigDecimal totalPrice;
    private  final Map<String, Integer> products;

    public Receipt(Cashier cashier, Map<String, Integer> products, int itemQuantity){
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
    public Map<String, Integer> getProducts(){
        return products;
    }

}
