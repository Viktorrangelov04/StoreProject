package domain.store;

import domain.product.Category;
import domain.product.Product;
import domain.product.StockItem;
import pricing.MarkupStrategy;
import pricing.PricingStrategy;
import services.EmployeeManager;
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
    private final PurchaseManager purchaseManager;
    private final ReceiptStorage receiptStorage;
    private final PricingStrategy pricingStrategy;

    private final Map<StockItem, Integer> cart = new HashMap<>();
    ReceiptStorage storage = new FileReceiptStorage();

    public Store(InventoryManager inventoryManager, EmployeeManager employeeManager, PurchaseManager purchaseManager, ReceiptStorage receiptStorage, PricingStrategy pricingStrategy, String storeName, BigDecimal expiryDiscountPercent, int daysBeforeExpirationThreshold){
        this.id = UUID.randomUUID().toString();

        //New code
        this.inventoryManager = inventoryManager;
        this.employeeManager = employeeManager;
        this.purchaseManager = purchaseManager;
        this.receiptStorage = receiptStorage;
        this.pricingStrategy = pricingStrategy;
        //New code

        this.storeName = storeName;
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

    public void addToCart(StockItem item, int quantity){
        int current = cart.getOrDefault(item, 0);
        cart.put(item, current+quantity);
    }




    //UPDATED METHDOS AFTER ENCAPSULATION

    public void addStock(Product product, int quantity, BigDecimal deliveryPrice, LocalDate expiryDate){
        StockItem item = new StockItem(product, deliveryPrice, quantity, expiryDate);
        BigDecimal sellingPrice = pricingStrategy.calculateSellingPrice(item.getDeliveryPrice(), item.getProduct().getCategory());
        item.setSellingPrice(sellingPrice);
        inventoryManager.addStock(item);
    }

}
