package services;

import domain.store.Cashier;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ITFinancialManagerTest {

    @Test
    void getStoreRevenue() {
        //Arrange
        FinancialManager financialManager = new FinancialManager();
        //Assert
        assertEquals(BigDecimal.ZERO, financialManager.getStoreRevenue());
    }

    @Test
    void getTotalDeliveryCost() {
        //Arrange
        FinancialManager financialManager = new FinancialManager();
        //Assert
        assertEquals(BigDecimal.ZERO, financialManager.getTotalDeliveryCost());
    }

    @Test
    void addDeliveryCost_ShouldIncreaseDeliveryCost() {
        //Arrange
        FinancialManager financialManager = new FinancialManager();
        //Act
        financialManager.addDeliveryCost(new BigDecimal("1000"));
        //Assert
        assertEquals(new BigDecimal("1000"), financialManager.getTotalDeliveryCost());
    }

    @Test
    void addRevenue_ShouldIncreaseRevenue() {
        //Arrange
        FinancialManager financialManager = new FinancialManager();
        //Act
        financialManager.addRevenue(new BigDecimal("1000"));
        //Assert
        assertEquals(new BigDecimal("1000"), financialManager.getStoreRevenue());
    }

    @Test
    void calculateProfit_ShouldReturnProfit() {
        // Arrange
        FinancialManager financialManager = new FinancialManager();
        financialManager.addRevenue(new BigDecimal("1000"));
        financialManager.addDeliveryCost(new BigDecimal("300"));

        List<Cashier> cashiers = List.of(
                new Cashier("Viktor", new BigDecimal("200")),
                new Cashier("Martin", new BigDecimal("150"))
        );
        // Act
        BigDecimal profit = financialManager.calculateProfit(cashiers);

        // Assert
        assertEquals(new BigDecimal("350"), profit);
    }
}