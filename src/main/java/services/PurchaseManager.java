package services;

import exceptions.InsufficientStockException;
import exceptions.InsufficientFundsException;
import domain.product.Product;
import domain.product.StockItem;
import domain.receipt.Receipt;
import domain.store.Cashier;
import pricing.ExpiryDiscountStrategy;
import storage.ReceiptStorage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class PurchaseManager {
    private final InventoryManager inventoryManager;
    private final ReceiptStorage receiptStorage;
    private final FinancialManager financialManager;
    private final ExpiryDiscountStrategy expiryDiscountStrategy;

    private final Map<StockItem, Integer> cart = new HashMap<>();

    public PurchaseManager(InventoryManager inventoryManager,
                           ReceiptStorage receiptStorage,
                           FinancialManager financialManager,
                           ExpiryDiscountStrategy expiryDiscountStrategy) {
        this.inventoryManager = inventoryManager;
        this.receiptStorage = receiptStorage;
        this.financialManager = financialManager;
        this.expiryDiscountStrategy = expiryDiscountStrategy;
    }

    public void addToCart(StockItem item, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        int current = cart.getOrDefault(item, 0);
        cart.put(item, current + quantity);
    }

    public void clearCart() {
        cart.clear();
    }

    public Map<StockItem, Integer> getCart() {
        return new HashMap<>(cart); // Return copy for safety
    }

    public void processPurchase(Scanner scanner, BigDecimal clientMoney, Cashier cashier)
            throws InsufficientStockException, InsufficientFundsException {
        clearCart();
        buildCart(scanner);
        BigDecimal totalCost = validateAndCalculateTotal();
        validatePayment(clientMoney, totalCost);
        completeSale(cashier, totalCost);

        System.out.println("Purchase completed successfully!");
        System.out.println("Change: " + clientMoney.subtract(totalCost).setScale(2, RoundingMode.HALF_UP) + " BGN");
    }

    private void buildCart(Scanner scanner) throws InsufficientStockException {
        while (true) {
            System.out.print("Insert product name (or 'done' to finish): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("done")) {
                break;
            }

            Product product = inventoryManager.findProductByName(input);
            if (product == null) {
                System.out.println("Product not found. Please try again.");
                continue;
            }

            int quantity = getQuantityFromUser(scanner);
            if (quantity <= 0) {
                continue;
            }

            addProductToCart(product, quantity);
        }
    }

    private int getQuantityFromUser(Scanner scanner) {
        System.out.print("Quantity: ");
        try {
            int quantity = Integer.parseInt(scanner.nextLine().trim());
            if (quantity <= 0) {
                System.out.println("Please enter a positive number.");
                return -1;
            }
            return quantity;
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity. Please enter a valid number.");
            return -1;
        }
    }

    private void addProductToCart(Product product, int quantity) throws InsufficientStockException {
        List<StockItem> stockItems = inventoryManager.getStockForProduct(product);

        if (stockItems == null || stockItems.isEmpty()) {
            System.out.println("Product is out of stock");
            return;
        }

        int totalAvailable = stockItems.stream()
                .filter(item -> item.getQuantity() > 0 && !item.isExpired())
                .mapToInt(StockItem::getQuantity)
                .sum();

        if (totalAvailable < quantity) {
            throw new InsufficientStockException(stockItems.getFirst(), quantity, totalAvailable);
        }

        StockItem item = inventoryManager.getFirstAvailableStockItem(product);
        if (item != null) {
            addToCart(item, quantity);
            System.out.println("Added " + quantity + " x " + product.getName() + " to cart");
        }
    }

    private BigDecimal validateAndCalculateTotal() throws InsufficientStockException {
        BigDecimal totalCost = BigDecimal.ZERO;

        for (Map.Entry<StockItem, Integer> entry : cart.entrySet()) {
            StockItem item = entry.getKey();
            int quantity = entry.getValue();

            if (item.getQuantity() < quantity) {
                throw new InsufficientStockException(item, quantity, item.getQuantity());
            }

            if (item.isExpired()) {
                System.out.println("Warning: Product \"" + item.getProduct().getName() + "\" is expired and will be removed from cart");
                continue;
            }

            expiryDiscountStrategy.applyDiscountIfNeeded(item);

            BigDecimal itemCost = item.getSellingPrice().multiply(BigDecimal.valueOf(quantity));
            totalCost = totalCost.add(itemCost);
        }

        System.out.println("Total cost: " + totalCost.setScale(2, RoundingMode.HALF_UP) + " BGN");
        return totalCost;
    }

    private void validatePayment(BigDecimal clientMoney, BigDecimal totalCost) throws InsufficientFundsException {
        if (clientMoney.compareTo(totalCost) < 0) {
            throw new InsufficientFundsException(totalCost, clientMoney);
        }
    }

    private void completeSale(Cashier cashier, BigDecimal totalCost){
        financialManager.addRevenue(totalCost);

        for (Map.Entry<StockItem, Integer> entry : cart.entrySet()) {
            StockItem item = entry.getKey();
            int quantity = entry.getValue();

            if (!item.isExpired()) {
                item.setQuantity(item.getQuantity() - quantity);
            }
        }

        int serialNumber = receiptStorage.getNextReceiptNumber();
        Receipt receipt = new Receipt(serialNumber, cashier, new HashMap<>(cart), cart.size(), totalCost);
        receiptStorage.saveReceipt(receipt);

        clearCart();
    }
}
