package services;

import domain.product.Category;
import domain.store.Store;
import pricing.ExpiryDiscountStrategy;
import pricing.MarkupStrategy;
import pricing.PricingStrategy;
import storage.FileReceiptStorage;
import storage.ReceiptStorage;

import java.math.BigDecimal;
import java.util.Map;

public class StoreFactory {

    public static Store createDefaultStore(String storeName) {
        Map<Category, BigDecimal> markupMap = Map.of(
                Category.Food, new BigDecimal("20"),
                Category.NonFood, new BigDecimal("30")
        );

        PricingStrategy pricingStrategy = new MarkupStrategy(markupMap);
        ExpiryDiscountStrategy expiryDiscountStrategy = new ExpiryDiscountStrategy(new BigDecimal("15"), 7);
        InventoryManager inventoryManager = new InventoryManager();
        EmployeeManager employeeManager = new EmployeeManager();
        FinancialManager financialManager = new FinancialManager(BigDecimal.ZERO, BigDecimal.ZERO);
        ReceiptStorage receiptStorage = new FileReceiptStorage();
        PurchaseManager purchaseManager = new PurchaseManager(inventoryManager, receiptStorage, financialManager, expiryDiscountStrategy);
        return new Store(
                storeName,
                inventoryManager,
                employeeManager,
                financialManager,
                pricingStrategy,
                expiryDiscountStrategy,
                receiptStorage,
                purchaseManager
        );
    }
}
