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

        Store store = new Store("MinimartSeniche", BigDecimal.valueOf(20), BigDecimal.valueOf(30), BigDecimal.valueOf(15), 10 );

    }
}