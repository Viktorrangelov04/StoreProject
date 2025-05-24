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

    public static Store createStore(String storeName, BigDecimal FoodMarkup,
                                           BigDecimal NonFoodMarkup, BigDecimal expiryDiscount,
                                           int expiryThreshold) {
        Map<Category, BigDecimal> markupMap = Map.of(
                Category.Food, FoodMarkup,
                Category.NonFood, NonFoodMarkup
        );

        PricingStrategy pricingStrategy = new MarkupStrategy(markupMap);
        ExpiryDiscountStrategy expiryDiscountStrategy = new ExpiryDiscountStrategy(expiryDiscount, expiryThreshold);
        InventoryManager inventoryManager = new InventoryManager();
        EmployeeManager employeeManager = new EmployeeManager();
        FinancialManager financialManager = new FinancialManager();
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
    public static Store createDefaultStore(String storeName) {
        return createStore(
                storeName,
                new BigDecimal("20"),
                new BigDecimal("30"),
                new BigDecimal("15"),
                7
        );
    }
}
