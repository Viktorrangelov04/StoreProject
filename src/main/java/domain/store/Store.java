package domain.store;

import domain.receipt.Receipt;
import domain.product.Category;
import domain.product.Product;
import domain.product.StockItem;
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
    private final PricingStrategy pricingStrategy;
    private final EmployeeManager employeeManager;
    private final PurchaseManager purchaseManager;
    private final ReceiptStorage receiptStorage;

    private BigDecimal expiryDiscountPercent;
    private int daysBeforeExpirationThreshold;

    private final Map<StockItem, Integer> cart = new HashMap<>();
    ReceiptStorage storage = new FileReceiptStorage();

    public Store(InventoryManager inventoryManager,PricingStrategy pricingStrategy, EmployeeManager employeeManager, PurchaseManager purchaseManager, ReceiptStorage receiptStorage, String storeName,   BigDecimal expiryDiscountPercent, int daysBeforeExpirationThreshold){
        this.id = UUID.randomUUID().toString();

        //New code
        this.inventoryManager = inventoryManager;
        this.pricingStrategy = pricingStrategy;
        this.employeeManager = employeeManager;
        this.purchaseManager = purchaseManager;
        this.receiptStorage = receiptStorage;
        //New code

        this.storeName = storeName;
        this.expiryDiscountPercent = expiryDiscountPercent;
        this.daysBeforeExpirationThreshold = daysBeforeExpirationThreshold;
    }

    public String getId(){
        return id;
    }
    public String getStoreName(){
        return storeName;
    }
    public BigDecimal getFoodMarkupPercent() {
        return markupPercentages.get(Category.Food);
    }
    public int getDaysBeforeExpirationThreshold(){
        return daysBeforeExpirationThreshold;
    }
    public BigDecimal getExpiryDiscountPercent(){
        return expiryDiscountPercent;
    }
    public BigDecimal getMarkup(Category category){
        return markupPercentages.getOrDefault(category, BigDecimal.ZERO);
    }
    public BigDecimal getTotalSalaries() {
        return cashiers.stream()
                .map(Cashier::getSalary)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalExpenses() {
        return totalDeliveryCost.add(getTotalSalaries());
    }

    public List<Cashier> getCashiers() {
        return cashiers;
    }

    public void setStoreName(String NewStoreName){
        storeName = NewStoreName;
    }
    public void setExpiryDiscountPercent(BigDecimal newPercent){
        if(newPercent.compareTo(BigDecimal.ZERO)<0 || newPercent.compareTo(BigDecimal.valueOf(100))>0){
            throw new IllegalArgumentException("Percent has to be between 0 and 100%");
        }
        expiryDiscountPercent = newPercent;
    }
//    public void setDaysBeforeExpirationThreshold(int newThreshold){
//        daysBeforeExpirationThreshold = newThreshold;
//    }
//    public void setMarkup(Category category, BigDecimal newPercent){
//        if(newPercent.compareTo(BigDecimal.ZERO)<0 || newPercent.compareTo(BigDecimal.valueOf(100))>0){
//            throw new IllegalArgumentException("Percent has to be between 0 and 100%");
//        }
//        markupPercentages.put(category, newPercent);
//    }

    public void addToCart(StockItem item, int quantity){
        int current = cart.getOrDefault(item, 0);
        cart.put(item, current+quantity);
    }




    //UPDATED METHDOS AFTER ENCAPSULATION

    public void addStock(Product product, int quantity, BigDecimal deliveryPrice, LocalDate expiryDate){
        StockItem item = new StockItem(product, deliveryPrice, quantity, expiryDate);
        BigDecimal sellingPrice = pricingStrategy.calculateSellingPrice(item);
        item.setSellingPrice(sellingPrice);
        inventoryManager.addStock(item);
    }

}
