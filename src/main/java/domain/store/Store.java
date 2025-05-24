package domain.store;

import domain.product.Category;
import domain.product.Product;
import domain.product.StockItem;
import pricing.ExpiryDiscountStrategy;
import pricing.MarkupStrategy;
import pricing.PricingStrategy;
import services.EmployeeManager;
import services.FinancialManager;
import services.InventoryManager;
import services.PurchaseManager;
import storage.FileReceiptStorage;
import storage.ReceiptStorage;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class Store {
    private final String id;
    private String storeName;

    private final InventoryManager inventoryManager;
    private final EmployeeManager employeeManager;
    private final FinancialManager financialManager;
    private final PricingStrategy pricingStrategy;
    private final ExpiryDiscountStrategy expiryDiscountStrategy;

    private final ReceiptStorage receiptStorage;
    private final PurchaseManager purchaseManager;



    public Store(String storeName,
                 InventoryManager inventoryManager,
                 EmployeeManager employeeManager,
                 FinancialManager financialManager,
                 PricingStrategy pricingStrategy,
                 ExpiryDiscountStrategy expiryDiscountStrategy,
                 ReceiptStorage receiptStorage,
                 PurchaseManager purchaseManager
                 ){
        this.id = UUID.randomUUID().toString();
        this.storeName = storeName;

        this.inventoryManager = inventoryManager;
        this.employeeManager = employeeManager;
        this.financialManager = financialManager;
        this.pricingStrategy = pricingStrategy;
        this.expiryDiscountStrategy = expiryDiscountStrategy;
        this.receiptStorage = receiptStorage;
        this.purchaseManager = purchaseManager;
    }

    public String getId(){
        return id;
    }
    public String getStoreName(){
        return storeName;
    }

    public void setStoreName(String NewStoreName){
        storeName = NewStoreName;
    }

//    public void setDaysBeforeExpirationThreshold(int newThreshold){
//        daysBeforeExpirationThreshold = newThreshold;
//    }






    //UPDATED METHDOS AFTER ENCAPSULATION

    public void addStock(Product product, int quantity, BigDecimal deliveryPrice, LocalDate expiryDate){
        StockItem item = new StockItem(product, deliveryPrice, quantity, expiryDate);
        BigDecimal sellingPrice = pricingStrategy.calculateSellingPrice(item.getDeliveryPrice(), item.getProduct().getCategory());
        item.setSellingPrice(sellingPrice);
        inventoryManager.addStock(item);
    }

}
