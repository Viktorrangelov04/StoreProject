package domain.store;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CashierTest {

    @Test
    void getId() {
        //Arrange
        Cashier cashier = new Cashier("Viktor", new BigDecimal("1200"));
        //Assert
        assertNotNull(cashier.getId());
    }

    @Test
    void getName() {
        //Arrange
        Cashier cashier = new Cashier("Viktor", new BigDecimal("1200"));
        //Assert
        assertEquals("Viktor", cashier.getName());
    }

    @Test
    void getSalary() {
        //Arrange
        Cashier cashier = new Cashier("Viktor", new BigDecimal("1200"));
        //Assert
        assertEquals(new BigDecimal("1200"), cashier.getSalary());
    }

    @Test
    void setSalary() {
        //Arrange
        Cashier cashier = new Cashier("Viktor", new BigDecimal("1200"));
        //Act
        cashier.setSalary(new BigDecimal("1500"));
        //Asset
        assertEquals(new BigDecimal("1500"), cashier.getSalary());
    }
}