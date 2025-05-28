package app;

import domain.product.Category;
import domain.product.Product;
import domain.store.Cashier;
import domain.store.Store;
import exceptions.InsufficientFundsException;
import exceptions.InsufficientStockException;
import services.StoreFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("1. Създаване на магазин");
        Store store = StoreFactory.createStore(
                "Minimart",
                new BigDecimal("25"),
                new BigDecimal("35"),
                new BigDecimal("20"),
                5
        );
        System.out.println("Магазин създаден успешно: " + store.getStoreName());
        System.out.println("Надценка за хранителни стоки: " + store.getMarkup(Category.Food) + "%");
        System.out.println("Надценка за нехранителни стоки: " + store.getMarkup(Category.NonFood) + "%\n");

        System.out.println("2. Добавяне на касиери");
        Cashier cashier1 = store.addCashier("Мария Петрова", new BigDecimal("1500"));
        Cashier cashier2 = store.addCashier("Петър Иванов", new BigDecimal("1400"));
        Cashier cashier3 = store.addCashier("Елена Георгиева", new BigDecimal("1600"));
        System.out.println("Добавени касиери: " + cashier1.getName() + ", " +
                cashier2.getName() + ", " + cashier3.getName());

        System.out.println("3. Създаване на касови апарати");
        store.addCashRegistry();
        store.addCashRegistry();
        store.addCashRegistry();

        store.assignToFirstAvailableRegister(cashier1);
        store.assignToFirstAvailableRegister(cashier2);
        store.assignToFirstAvailableRegister(cashier3);
        System.out.println("Касиерите са назначени на касите\n");

        System.out.println("4. Зареждане на стоки в магазина");

        Product apple = new Product("Ябълки", Category.Food);
        Product bread = new Product("Хляб", Category.Food);
        Product milk = new Product("Мляко", Category.Food);
        Product cheese = new Product("Сирене", Category.Food);

        Product shampoo = new Product("Шампоан", Category.NonFood);
        Product notebook = new Product("Тетрадка", Category.NonFood);
        Product batteries = new Product("Батерии", Category.NonFood);

        store.addStock(apple, 50, new BigDecimal("2.00"), LocalDate.now().plusDays(10));
        store.addStock(bread, 30, new BigDecimal("1.20"), LocalDate.now().plusDays(3));
        store.addStock(milk, 25, new BigDecimal("2.50"), LocalDate.now().plusDays(7));
        store.addStock(cheese, 15, new BigDecimal("8.00"), LocalDate.now().plusDays(2));

        store.addStock(shampoo, 20, new BigDecimal("5.00"), LocalDate.now().plusDays(365));
        store.addStock(notebook, 40, new BigDecimal("1.50"), LocalDate.now().plusDays(365));
        store.addStock(batteries, 35, new BigDecimal("3.00"), LocalDate.now().plusDays(365));

        System.out.println("Добавени хранителни стоки: ябълки, хляб, мляко, сирене");
        System.out.println("Добавени нехранителни стоки: шампоан, тетрадка, батерии");
        System.out.println("Някои стоки са близо до изтичане на срока (ще имат отстъпка)\n");

        System.out.println("5. Финансово състояние преди продажби:");
        System.out.println("Разходи за доставки: " + store.getTotalDeliveryCost() + " лв.");
        System.out.println("Приходи от продажби: " + store.getStoreRevenue() + " лв.");
        System.out.println("Печалба: " + store.getProfit() + " лв.\n");

        System.out.println("6. Симулация на покупки...\n");

        System.out.println("ПОКУПКА 1");
        simulatePurchase(store, cashier1, new BigDecimal("30.00"),
                "Ябълки,5;Хляб,2;Мляко,1");

        System.out.println("\nПОКУПКА 2");
        simulatePurchase(store, cashier2, new BigDecimal("25.00"),
                "Шампоан,1;Тетрадка,3");

        System.out.println("\nПОКУПКА 3");
        simulatePurchase(store, cashier3, new BigDecimal("5.00"),
                "Сирене,2;Батерии,1");

        System.out.println("\nПОКУПКА 4");
        simulatePurchase(store, cashier1, new BigDecimal("100.00"),
                "Ябълки,100");

        System.out.println("\n7. Финално финансово състояние:");
        System.out.println("Разходи за доставки: " + store.getTotalDeliveryCost() + " лв.");
        System.out.println("Приходи от продажби: " + store.getStoreRevenue() + " лв.");
        System.out.println("Печалба: " + store.getProfit() + " лв.");

        System.out.println("\n8.Информация за касови бележки:");
        System.out.println("Брой издадени касови бележки: " +
                store.getReceiptStorage().getTotalReceiptsIssued());
        System.out.println("Касовите бележки са запазени в отделни файлове");

        System.out.println("\n9. Интерактивна покупка (въведете 'demo' за демо или друго за пропускане):");
        Scanner realScanner = new Scanner(System.in);
        String choice = realScanner.nextLine();

        if (choice.equalsIgnoreCase("demo")) {
            System.out.println("Започване на интерактивна покупка");
            System.out.println("Достъпни стоки: Ябълки, Хляб, Мляко, Сирене, Шампоан, Тетрадка, Батерии");
            try {
                store.processPurchase(realScanner, new BigDecimal("50.00"), cashier1);
            } catch (Exception e) {
                System.out.println("Покупката неуспешна: " + e.getMessage());
            }
        }
    }

    private static void simulatePurchase(Store store, Cashier cashier,
                                         BigDecimal clientMoney, String items) {
        try {
            System.out.println("Касиер: " + cashier.getName());
            System.out.println("Пари на клиента: " + clientMoney + " лв.");
            System.out.println("Желани стоки: " + items);

            Scanner mockScanner = createMockScanner(items);

            store.processPurchase(mockScanner, clientMoney, cashier);
            System.out.println("Покупката е успешна!");

        } catch (InsufficientStockException e) {
            System.out.println("Покупката неуспешна - недостатъчно количество:");
            System.out.println("  " + e.getMessage());
        } catch (InsufficientFundsException e) {
            System.out.println("Покупката неуспешна - недостатъчно пари:");
            System.out.println("  " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Покупката неуспешна - грешка:");
            System.out.println("  " + e.getMessage());
        }
    }

    private static Scanner createMockScanner(String items) {
        StringBuilder input = new StringBuilder();

        String[] itemPairs = items.split(";");

        for (String pair : itemPairs) {
            String[] parts = pair.split(",");
            input.append(parts[0].trim()).append("\n");
            input.append(parts[1].trim()).append("\n");
        }

        input.append("done\n");

        return new Scanner(input.toString());
    }
}
