package services;

import domain.product.InsufficientStockException;
import domain.product.Product;
import domain.product.StockItem;
import domain.receipt.Receipt;
import domain.store.Cashier;
import storage.ReceiptStorage;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class PurchaseManager {
    private final InventoryManager inventoryManager;
    private final ReceiptStorage receiptStorage;

    public void buyProducts(Scanner scanner, BigDecimal clientMoney, Cashier cashier) throws InsufficientStockException, IOException {
        cart.clear();
        int cartQuantity = 0;
        BigDecimal totalCost = BigDecimal.ZERO;

        while (true) {
            System.out.print("Insert product name(or 'done' to finalize purchase): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("done")) {
                break;
            }

            Product product = InventoryManager.findProductByName(input);
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
                System.out.println("Invalid quantity. Please enter a valid number.");
                continue;
            }

            List<StockItem> stockItems = inventory.get(product);
            if (stockItems == null || stockItems.isEmpty()) {
                System.out.println("Product is out of stock");
                continue;
            }

            // Сумираме всички налични и валидни StockItem-и за този продукт
            int totalAvailable = stockItems.stream()
                    .filter(item -> item.getQuantity() > 0 && !item.isExpired())
                    .mapToInt(StockItem::getQuantity)
                    .sum();

            if (totalAvailable < quantity) {
                throw new InsufficientStockException(stockItems.get(0), quantity, totalAvailable);
            }

            // Засега добавяме само първия валиден stock item, както беше в оригинала
            StockItem item = getFirstAvailableStockItem(product);
            if (item != null) {
                addToCart(item, quantity);
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

            if (item.isCloseToExpiry(daysBeforeExpirationThreshold)) {
                item.applyCloseToExpiryDiscount(expiryDiscountPercent, daysBeforeExpirationThreshold);
            }

            totalCost = totalCost.add(item.getSellingPrice().multiply(BigDecimal.valueOf(quantity)));
        }

        System.out.println("Total cost: " + totalCost.setScale(2, RoundingMode.HALF_UP) + " BGN");

        if (clientMoney.compareTo(totalCost) < 0) {
            BigDecimal need = totalCost.subtract(clientMoney);
            System.out.println("You don't have enough money. You need: " + need + " BGN more");
            return;
        }

        storeRevenue = storeRevenue.add(totalCost);

        for (Map.Entry<StockItem, Integer> entry : cart.entrySet()) {
            StockItem item = entry.getKey();
            int quantity = entry.getValue();

            int newQuantity = item.getQuantity() - quantity;
            item.setQuantity(newQuantity);

            BigDecimal profit = item.getSellingPrice().multiply(BigDecimal.valueOf(quantity))
                    .subtract(item.getDeliveryPrice().multiply(BigDecimal.valueOf(quantity)));
            storeProfit = storeProfit.add(profit);
            cartQuantity++;
        }

        int serialNumber = storage.getNextReceiptNumber();
        Receipt receipt = new Receipt(serialNumber, cashier, cart, cartQuantity, totalCost);
        storage.saveReceipt(receipt);
    }
}
