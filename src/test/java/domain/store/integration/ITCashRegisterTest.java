package domain.store.integration;

import domain.store.CashRegister;
import domain.store.Cashier;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ITCashRegisterTest {

    @Test
    void isAvailable_ShouldReturnTrue_IfAvailable() {
        //Arrange
        CashRegister cashRegister = new CashRegister();
        //Assert
        assertTrue(cashRegister.isAvailable());
    }

    @Test
    void assign_ShouldAssignCorrectly_AndBecomeUnavailable() {
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
    void unassign_ShouldUnassign() {
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
    void getAssignedCashier_ShouldReturnCashier() {
        //Arrange
        Cashier cashier = new Cashier("Viktor", new BigDecimal("1200"));
        CashRegister cashRegister = new CashRegister();
        //Act
        cashRegister.assign(cashier);
        //Assert
        assertEquals(cashier, cashRegister.getAssignedCashier());
    }
}