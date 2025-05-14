package Products;

public class InsufficientStockException extends Exception {
    public InsufficientStockException(StockItem item, int requested, int available) {
        super("Insufficient stock from \"" + item.getProduct().getName() +
                "\". ordered: " + requested + ", available: " + available);
    }
}