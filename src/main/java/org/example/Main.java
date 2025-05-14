package org.example;

import Products.Category;
import Products.Product;
import Products.StockItem;
import Store.Store;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        BigDecimal clientMoney= BigDecimal.valueOf(100);

        Product cheese = new Product("Сирене", Category.Food);

        StockItem batch1 = new StockItem(new BigDecimal("3.00"), new BigDecimal("3.60"), 50, LocalDate.of(2025, 6, 1));
        StockItem batch2 = new StockItem(new BigDecimal("2.90"), new BigDecimal("3.40"), 30, LocalDate.of(2025, 6, 15));

        Store store = new Store("MinimartSeniche", BigDecimal.valueOf(20), BigDecimal.valueOf(30), BigDecimal.valueOf(15), 10 );
        store.addStock(cheese, batch1);
        store.addStock(cheese, batch2);

    }
}