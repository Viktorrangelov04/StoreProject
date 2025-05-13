package Store;

import Products.Category;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Store {
    private final String id;
    private String storeName;
    private BigDecimal expiryDiscountPercent;
    private int daysBeforeExpirationThreshold;
    private final Map<Category, BigDecimal> markupPercentages = new HashMap<>();

    public Store(String storeName, BigDecimal expiryDiscountPercent, int daysBeforeExpirationThreshold, BigDecimal foodMarkupPercent, BigDecimal nonFoodMarkupPercent){
        this.id = UUID.randomUUID().toString();
        this.storeName = storeName;
        this.expiryDiscountPercent = expiryDiscountPercent;
        this.daysBeforeExpirationThreshold = daysBeforeExpirationThreshold;
        markupPercentages.put(Category.NonFood, nonFoodMarkupPercent);
        markupPercentages.put(Category.Food, foodMarkupPercent);
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

    public void setStoreName(String storeName){
        this.storeName = storeName;
    }
    public void setExpiryDiscountPercent(BigDecimal newExpiryDiscountPercent){
        if(newExpiryDiscountPercent.compareTo(BigDecimal.ZERO)<0 || newExpiryDiscountPercent.compareTo(BigDecimal.valueOf(100))>0){
            throw new IllegalArgumentException("Percent has to be between 0 and 100%");
        }
        this.expiryDiscountPercent = newExpiryDiscountPercent;
    }
    public void setDaysBeforeExpirationThreshold(int daysBeforeExpirationThreshold){
        this.daysBeforeExpirationThreshold = daysBeforeExpirationThreshold;
    }
    public void setMarkup(Category category, BigDecimal percent){
        markupPercentages.put(category, percent);
    }
}
