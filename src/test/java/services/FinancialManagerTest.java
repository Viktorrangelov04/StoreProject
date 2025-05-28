package services;

import domain.store.Cashier;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class FinancialManagerTest {
    @Test
    void addRevenue_ShouldIncreaseRevenue() {
        FinancialManager manager = new FinancialManager();
        manager.addRevenue(new BigDecimal("100.00"));
        assertEquals(new BigDecimal("100.00"), manager.getStoreRevenue());
    }

    @Test
    void addDeliveryCost_ShouldIncreaseDeliveryCost() {
        FinancialManager manager = new FinancialManager();
        manager.addDeliveryCost(new BigDecimal("50.00"));
        assertEquals(new BigDecimal("50.00"), manager.getTotalDeliveryCost());
    }

    @Test
    void calculateProfit_ShouldReturnCorrectProfit() {
        FinancialManager manager = new FinancialManager();
        manager.addRevenue(new BigDecimal("1000.00"));
        manager.addDeliveryCost(new BigDecimal("200.00"));

        Cashier c1 = new Cashier("Anna", new BigDecimal("300.00"));
        Cashier c2 = new Cashier("Bob", new BigDecimal("200.00"));

        BigDecimal profit = manager.calculateProfit(Arrays.asList(c1, c2));

        assertEquals(new BigDecimal("300.00"), profit);
    }

    @Test
    void calculateProfit_WithNoCashiers_ShouldReturnRevenueMinusDeliveryCost() {
        FinancialManager manager = new FinancialManager();
        manager.addRevenue(new BigDecimal("500.00"));
        manager.addDeliveryCost(new BigDecimal("100.00"));

        BigDecimal profit = manager.calculateProfit(Collections.emptyList());
        assertEquals(new BigDecimal("400.00"), profit);
    }
}
