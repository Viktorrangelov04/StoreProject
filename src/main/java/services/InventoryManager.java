package services;

import domain.product.Product;
import domain.product.StockItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryManager {
    private final Map<Product, List<StockItem>> inventory = new HashMap<>();

    public Map<Product, List<StockItem>> getInventory() {
        return new HashMap<>(inventory);
    }

    public void addStock(StockItem newItem) {
        inventory.computeIfAbsent(newItem.getProduct(), k -> new ArrayList<>()).add(newItem);
    }

    public Product findProductByName(String name){
        for(Product p: inventory.keySet()){
            if(p.getName().equalsIgnoreCase(name)){
                return p;
            }
        }
        return null;
    }

    public List<StockItem> getStockForProduct(Product product) {
        return inventory.getOrDefault(product, new ArrayList<>());
    }

    public StockItem getFirstAvailableStockItem(Product product) {
        List<StockItem> stockList = inventory.get(product);
        if (stockList == null || stockList.isEmpty()) return null;

        for (StockItem item : stockList) {
            if (item.getQuantity() > 0 && !item.isExpired()) {
                return item;
            }
        }
        return null;
    }
}
