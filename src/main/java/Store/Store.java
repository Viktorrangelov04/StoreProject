package Store;

import Products.Category;
import Products.Product;
import Products.StockItem;

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

    public Store(String storeName, BigDecimal foodMarkupPercent, BigDecimal nonFoodMarkupPercent, BigDecimal expiryDiscountPercent, int daysBeforeExpirationThreshold){
        this.id = UUID.randomUUID().toString();
        this.storeName = storeName;
        markupPercentages.put(Category.NonFood, nonFoodMarkupPercent);
        markupPercentages.put(Category.Food, foodMarkupPercent);
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
    public BigDecimal getMarkup(Category category){
        return markupPercentages.getOrDefault(category, BigDecimal.ZERO);
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

    public void addStock(Product product, StockItem newItem) {
        BigDecimal markup = this.getMarkup(product.getCategory());
        BigDecimal markupRate = markup.divide(new BigDecimal("100"));
        BigDecimal sellingPrice = newItem.getDeliveryPrice()
                .multiply(BigDecimal.ONE.add(markupRate))
                .setScale(2, RoundingMode.HALF_UP);
        newItem.setSellingPrice(sellingPrice);

        inventory.computeIfAbsent(product, k -> new ArrayList<>()).add(newItem);
    }

    public void addStock(Product product, int quantity, BigDecimal deliveryPrice, LocalDate expiryDate) {
        BigDecimal markup = this.getMarkup(product.getCategory());
        BigDecimal markupRate = markup.divide(new BigDecimal("100"));
        BigDecimal sellingPrice = deliveryPrice
                .multiply(BigDecimal.ONE.add(markupRate))
                .setScale(2, RoundingMode.HALF_UP);

        StockItem newItem = new StockItem(deliveryPrice, quantity, expiryDate);
        newItem.setSellingPrice(sellingPrice);

        inventory.computeIfAbsent(product, k -> new ArrayList<>()).add(newItem);
    }
}
