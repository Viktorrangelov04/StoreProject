package services;

import exceptions.InsufficientStockException;
import domain.product.Product;
import domain.product.StockItem;
import domain.receipt.Receipt;
import domain.store.Cashier;
import pricing.ExpiryDiscountStrategy;
import storage.ReceiptStorage;

import java.io.IOException;
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

    public void addToCart(StockItem item, int quantity){
        int current = cart.getOrDefault(item, 0);
        cart.put(item, current+quantity);
    }
    public void processPurchase(Scanner scanner, BigDecimal clientMoney, Cashier cashier) throws InsufficientStockException, IOException {
        Map<StockItem, Integer> cart = new HashMap<>();
        int cartQuantity = 0;
        BigDecimal totalCost = BigDecimal.ZERO;

        while (true) {
            System.out.print("Insert product name (or 'done'): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("done")) break;

            Product product = inventoryManager.findProductByName(input);
            if (product == null) {
                System.out.println("Product isn't found");
                continue;
            }

            System.out.print("Quantity: ");
            int quantity;
            try {
                quantity = Integer.parseInt(scanner.nextLine());
                if (quantity <= 0) {
                    System.out.println("Please enter a positive number.");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid quantity.");
                continue;
            }

            List<StockItem> stockItems = inventoryManager.getStockForProduct(product);
            if (stockItems == null || stockItems.isEmpty()) {
                System.out.println("Product is out of stock");
                continue;
            }

            int totalAvailable = stockItems.stream()
                    .filter(item -> item.getQuantity() > 0 && !item.isExpired())
                    .mapToInt(StockItem::getQuantity)
                    .sum();

            if (totalAvailable < quantity) {
                throw new InsufficientStockException(stockItems.get(0), quantity, totalAvailable);
            }

            StockItem item = inventoryManager.getFirstAvailableStockItem(product);
            if (item != null) {
                cart.put(item, cart.getOrDefault(item, 0) + quantity);
            }
        }

        for (Map.Entry<StockItem, Integer> entry : cart.entrySet()) {
            StockItem item = entry.getKey();
            int quantity = entry.getValue();

            if (item.getQuantity() < quantity) {
                throw new InsufficientStockException(item, quantity, item.getQuantity());
            }

            if (item.isExpired()) {
                System.out.println("Product \"" + item.getProduct().getName() + "\" is expired");
                continue;
            }

            expiryDiscountStrategy.applyDiscountIfNeeded(item);

            totalCost = totalCost.add(item.getSellingPrice().multiply(BigDecimal.valueOf(quantity)));
        }

        System.out.println("Total cost: " + totalCost.setScale(2, RoundingMode.HALF_UP) + " BGN");

        if (clientMoney.compareTo(totalCost) < 0) {
            BigDecimal need = totalCost.subtract(clientMoney);
            System.out.println("You don't have enough money. You need: " + need + " BGN more");
            return;
        }

        financialManager.addRevenue(totalCost);

        for (Map.Entry<StockItem, Integer> entry : cart.entrySet()) {
            StockItem item = entry.getKey();
            int quantity = entry.getValue();
            item.setQuantity(item.getQuantity() - quantity);
            cartQuantity++;
        }

        int serialNumber = receiptStorage.getNextReceiptNumber();
        Receipt receipt = new Receipt(serialNumber, cashier, cart, cartQuantity, totalCost);
        receiptStorage.saveReceipt(receipt);
    }

}
