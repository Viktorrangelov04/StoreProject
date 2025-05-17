package Store;

import Products.Category;
import Products.InsufficientStockException;
import Products.Product;
import Products.StockItem;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

public class Store {
    private final String id;
    private String storeName;
    private BigDecimal expiryDiscountPercent;
    private int daysBeforeExpirationThreshold;
    private final Map<Category, BigDecimal> markupPercentages = new HashMap<>();
    private final Map<Product, List<StockItem>> inventory = new HashMap<>();
    private final Map<StockItem, Integer> cart = new HashMap<>();
    private BigDecimal storeRevenue;
    private BigDecimal storeProfit;
    private BigDecimal totalDeliveryCost = BigDecimal.ZERO;
    private final List<Cashier> cashiers = new ArrayList<>();
    private final List<CashRegister> cashRegistries = new ArrayList<>();

    public Store(String storeName, BigDecimal foodMarkupPercent, BigDecimal nonFoodMarkupPercent, BigDecimal expiryDiscountPercent, int daysBeforeExpirationThreshold){
        this.id = UUID.randomUUID().toString();
        this.storeName = storeName;
        markupPercentages.put(Category.NonFood, nonFoodMarkupPercent);
        markupPercentages.put(Category.Food, foodMarkupPercent);
        this.expiryDiscountPercent = expiryDiscountPercent;
        this.daysBeforeExpirationThreshold = daysBeforeExpirationThreshold;
        this.storeRevenue = BigDecimal.ZERO;
        this.storeProfit = BigDecimal.ZERO;
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
    public BigDecimal getMarkup(Category category){
        return markupPercentages.getOrDefault(category, BigDecimal.ZERO);
    }
    public BigDecimal getTotalSalaries() {
        return cashiers.stream()
                .map(Cashier::getSalary)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalDeliveryCost() {
        return totalDeliveryCost;
    }

    public BigDecimal getTotalExpenses() {
        return totalDeliveryCost.add(getTotalSalaries());
    }

    public BigDecimal getProfit() {
        return storeRevenue.subtract(getTotalExpenses());
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
    public void setDaysBeforeExpirationThreshold(int newThreshold){
        daysBeforeExpirationThreshold = newThreshold;
    }
    public void setMarkup(Category category, BigDecimal newPercent){
        if(newPercent.compareTo(BigDecimal.ZERO)<0 || newPercent.compareTo(BigDecimal.valueOf(100))>0){
            throw new IllegalArgumentException("Percent has to be between 0 and 100%");
        }
        markupPercentages.put(category, newPercent);
    }

    public Cashier addCashier(String name, BigDecimal salary) {
        Cashier cashier = new Cashier(name, salary);
        cashiers.add(cashier);
        return cashier;
    }

    public CashRegister addCashRegistry() {
        CashRegister registry = new CashRegister();
        cashRegistries.add(registry);
        return registry;
    }

    public boolean assignToFirstAvailableRegister(Cashier cashier) {
        for (CashRegister registry : cashRegistries) {
            if (registry.isAvailable()) {
                registry.assign(cashier);
                System.out.println("Cashier " + cashier.getName() + " assigned to a register.");
                return true;
            }
        }
        System.out.println("No available registers for cashier " + cashier.getName());
        return false;
    }

    public void unassignWorkerFromCashRegistry(int index) {
        if (index < 0 || index >= cashRegistries.size()) {
            throw new IndexOutOfBoundsException("Invalid registry index");
        }
        cashRegistries.get(index).unassign();
    }

    public void addStock(Product product, StockItem newItem) {
        BigDecimal markup = this.getMarkup(product.getCategory());
        BigDecimal markupRate = markup.divide(new BigDecimal("100"));
        BigDecimal sellingPrice = newItem.getDeliveryPrice()
                .multiply(BigDecimal.ONE.add(markupRate))
                .setScale(2, RoundingMode.HALF_UP);
        newItem.setSellingPrice(sellingPrice);

        inventory.computeIfAbsent(product, k -> new ArrayList<>()).add(newItem);
    }

    public void addStock(Cashier cashier, Product product, int quantity, BigDecimal deliveryPrice, LocalDate expiryDate) {
        BigDecimal markup = this.getMarkup(product.getCategory());
        BigDecimal markupRate = markup.divide(new BigDecimal("100"));
        BigDecimal sellingPrice = deliveryPrice
                .multiply(BigDecimal.ONE.add(markupRate))
                .setScale(2, RoundingMode.HALF_UP);

        StockItem newItem = new StockItem(product, deliveryPrice, quantity, expiryDate);
        newItem.setSellingPrice(sellingPrice);

        BigDecimal deliveryTotal = deliveryPrice.multiply(BigDecimal.valueOf(quantity));
        totalDeliveryCost = totalDeliveryCost.add(deliveryTotal);
        inventory.computeIfAbsent(product, k -> new ArrayList<>()).add(newItem);
    }

    public Product findProductByName(String name){
        for(Product p: inventory.keySet()){
            if(p.getName().equalsIgnoreCase(name)){
                return p;
            }
        }
        return null;
    }

    private StockItem getFirstAvailableStockItem(Product product) {
        List<StockItem> stockList = inventory.get(product);
        if (stockList == null || stockList.isEmpty()) return null;

        for (StockItem item : stockList) {
            if (item.getQuantity() > 0 && !item.isExpired()) {
                return item;
            }
        }
        return null;
    }

    public void addToCart(StockItem item, int quantity){
        int current = cart.getOrDefault(item, 0);
        cart.put(item, current+quantity);

    }
    public void buyProducts(Scanner scanner, BigDecimal clientMoney, Cashier cashier) throws InsufficientStockException, IOException {
        cart.clear();
        int cartQuantity=0;
        BigDecimal totalCost = BigDecimal.ZERO;
        while(true){
            System.out.print("Insert product name(or 'done' to finalize purchase): ");
            String input = scanner.nextLine().trim();

            if(input.equals("done")){
                break;
            }

            Product product = findProductByName(input);
            if(product == null){
                System.out.println("Product isn't found");
                continue;
            }
            System.out.print("Quantity");
            int quantity = Integer.parseInt(scanner.nextLine());
            StockItem item = getFirstAvailableStockItem(product);
            if(item==null){
                System.out.println("Product is out of stock");
                continue;
            }

            addToCart(item, quantity);
        }
        for (Map.Entry<StockItem, Integer> entry : cart.entrySet()) {
            StockItem item = entry.getKey();
            int quantity = entry.getValue();

            if (item.getQuantity() < quantity) {
                throw new InsufficientStockException(item, quantity, item.getQuantity());
            }

            if(item.isExpired()){
                System.out.println("Product \"" + item.getProduct().getName() + "\" is expired");
                continue;
            }
            if(item.isCloseToExpiry(daysBeforeExpirationThreshold)){
                item.applyCloseToExpiryDiscount(expiryDiscountPercent);
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

            int newQuantity= item.getQuantity()-entry.getValue();
            item.setQuantity(newQuantity);

            BigDecimal profit = item.getSellingPrice().multiply(BigDecimal.valueOf(quantity))
                    .subtract(item.getDeliveryPrice().multiply(BigDecimal.valueOf(quantity)));
            storeProfit = storeProfit.add(profit);
            cartQuantity++;
        }

        int serialNumber = ReceiptManager.getNextReceiptNumber();
        Receipt receipt = new Receipt(serialNumber, cashier, cart, cartQuantity, totalCost);
        ReceiptManager.saveReceipt(receipt);
    }
}
