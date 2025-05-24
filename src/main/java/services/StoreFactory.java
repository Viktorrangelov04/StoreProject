package services;

import domain.product.Category;
import domain.store.Store;
import pricing.MarkupStrategy;
import pricing.PricingStrategy;
import storage.FileReceiptStorage;
import storage.ReceiptStorage;

import java.math.BigDecimal;
import java.util.Map;

public class StoreFactory {

    public static Store createDefaultStore(String storeName) {
        // Define store-specific markup configuration
        Map<Category, BigDecimal> markupPercentages = Map.of(
                Category.Food, new BigDecimal("20"),
                Category.NonFood, new BigDecimal("30")
        );
        PricingStrategy pricingStrategy = new MarkupStrategy(markupPercentages);

        MarkupStrategy markupStrategy = new MarkupStrategy(markupPercentages);

        InventoryManager inventoryManager = new InventoryManager();
        EmployeeManager employeeManager = new EmployeeManager();
        FinancialManager financialManager = new FinancialManager(BigDecimal.ZERO, BigDecimal.ZERO);
        PurchaseManager purchaseManager = new PurchaseManager();
        ReceiptStorage receiptStorage = new FileReceiptStorage();

        return new Store(
                inventoryManager,
                employeeManager,
                purchaseManager,
                receiptStorage,
                pricingStrategy,
                storeName,
                financialManager,

                markupStrategy
        );
    }
}
