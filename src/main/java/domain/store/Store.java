package domain.store;

import domain.product.Category;
import domain.product.Product;
import domain.product.StockItem;
import exceptions.InsufficientFundsException;
import exceptions.InsufficientStockException;
import pricing.ExpiryDiscountStrategy;
import pricing.MarkupStrategy;
import pricing.PricingStrategy;
import services.EmployeeManager;
import services.FinancialManager;
import services.InventoryManager;
import services.PurchaseManager;
import storage.FileReceiptStorage;
import storage.ReceiptStorage;

import java.io.IOException;
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

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public EmployeeManager getEmployeeManager() {
        return employeeManager;
    }

    public FinancialManager getFinancialManager() {
        return financialManager;
    }

    public PricingStrategy getPricingStrategy() {
        return pricingStrategy;
    }
    public ExpiryDiscountStrategy getExpiryDiscountStrategy() {
        return expiryDiscountStrategy;
    }

    public ReceiptStorage getReceiptStorage() {
        return receiptStorage;
    }

    public PurchaseManager getPurchaseManager() {
        return purchaseManager;
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

    public void addStock(Product product, int quantity, BigDecimal deliveryPrice, LocalDate expiryDate){
        StockItem item = new StockItem(product, deliveryPrice, quantity, expiryDate);
        BigDecimal sellingPrice = pricingStrategy.calculateSellingPrice(item.getDeliveryPrice(), item.getProduct().getCategory());
        item.setSellingPrice(sellingPrice);

        BigDecimal totalDeliveryCost = deliveryPrice.multiply(BigDecimal.valueOf(quantity));
        financialManager.addDeliveryCost(totalDeliveryCost);

        inventoryManager.addStock(item);
    }

    public Product findProductByName(String name) {
        return inventoryManager.findProductByName(name);
    }

    public List<StockItem> getStockForProduct(Product product) {
        return inventoryManager.getStockForProduct(product);
    }

    public Cashier addCashier(String name, BigDecimal salary) {
        return employeeManager.addCashier(name, salary);
    }

    public CashRegister addCashRegistry() {
        return employeeManager.addCashRegistry();
    }

    public boolean assignToFirstAvailableRegister(Cashier cashier) {
        return employeeManager.assignToFirstAvailableRegister(cashier);
    }

    public BigDecimal getStoreRevenue() {
        return financialManager.getStoreRevenue();
    }

    public BigDecimal getTotalDeliveryCost() {
        return financialManager.getTotalDeliveryCost();
    }

    public BigDecimal getProfit() {
        return financialManager.calculateProfit(employeeManager.getAllCashiers());
    }

    public void processPurchase(Scanner scanner, BigDecimal clientMoney, Cashier cashier)
            throws InsufficientStockException, InsufficientFundsException, IOException {
        purchaseManager.processPurchase(scanner, clientMoney, cashier);
    }

    public void setMarkup(Category category, BigDecimal newPercent) {
        if (pricingStrategy instanceof MarkupStrategy) {
            ((MarkupStrategy) pricingStrategy).setMarkup(category, newPercent);
        }
    }

    public BigDecimal getMarkup(Category category) {
        if (pricingStrategy instanceof MarkupStrategy) {
            return ((MarkupStrategy) pricingStrategy).getMarkup(category);
        }
        return BigDecimal.ZERO;
    }
}
