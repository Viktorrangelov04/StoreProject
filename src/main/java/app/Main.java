package app;

import domain.product.Category;
import domain.product.Product;
import domain.store.Cashier;
import domain.store.Store;
import services.StoreFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Store store = StoreFactory.createDefaultStore("My Grocery Store");

        // Add some products
        Product apple = new Product("Apple", Category.Food);
        store.addStock(apple, 100, new BigDecimal("1.50"), LocalDate.now().plusDays(7));

        // Add cashier and register
        Cashier cashier = store.addCashier("John Doe", new BigDecimal("2000"));
        store.addCashRegistry();
        store.assignToFirstAvailableRegister(cashier);

        // Process purchase
        Scanner scanner = new Scanner(System.in);
        try {
            store.processPurchase(scanner, new BigDecimal("50"), cashier);
        } catch (Exception e) {
            System.out.println("Purchase failed: " + e.getMessage());
        }

        System.out.println("Store profit: " + store.getProfit());
    }
}

