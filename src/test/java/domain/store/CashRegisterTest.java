package domain.store;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CashRegisterTest {

    @Test
    void isAvailable() {
        //Arrange
        CashRegister cashRegister = new CashRegister();
        //Assert
        assertTrue(cashRegister.isAvailable());
    }

    @Test
    void assign() {
        //Arrange
        Cashier cashier = new Cashier("Viktor", new BigDecimal("1200"));
        CashRegister cashRegister = new CashRegister();
        //Act
        cashRegister.assign(cashier);
        //Assert
        assertFalse(cashRegister.isAvailable());
        assertNotNull(cashRegister.getAssignedCashier());
        assertEquals("Viktor", cashRegister.getAssignedCashier().getName());
    }


    @Test
    void unassign() {
        //Arrange
        Cashier cashier = new Cashier("Viktor", new BigDecimal("1200"));
        CashRegister cashRegister = new CashRegister();
        //Act
        cashRegister.assign(cashier);
        cashRegister.unassign();
        //Assert
        assertTrue(cashRegister.isAvailable());
        assertNull(cashRegister.getAssignedCashier());
    }

    @Test
    void getAssignedCashier() {
        //Arrange
        Cashier cashier = new Cashier("Viktor", new BigDecimal("1200"));
        CashRegister cashRegister = new CashRegister();
        //Act
        cashRegister.assign(cashier);
        //Assert
        assertEquals(cashier, cashRegister.getAssignedCashier());
    }
}