package services;

import domain.store.CashRegister;
import domain.store.Cashier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ITEmployeeManagerTest {

    private EmployeeManager employeeManager;

    @BeforeEach
    void setUp() {
        employeeManager = new EmployeeManager();
    }

    @Test
    void addCashier_ShouldAddCashier() {
        //Arrange
        Cashier cashier = employeeManager.addCashier("Viktor", new BigDecimal("1200"));
        List<Cashier> cashiers = employeeManager.getAllCashiers();

        //Assert
        assertEquals(1, cashiers.size());
        assertEquals("Viktor", cashiers.getFirst().getName());
        assertEquals(new BigDecimal("1200"), cashiers.getFirst().getSalary());
        assertSame(cashier, cashiers.getFirst());
    }

    @Test
    void addCashRegister_ShouldAddCashRegister() {
        //Arrange
        CashRegister register = employeeManager.addCashRegistry();

        //Assert
        assertNotNull(register);
        assertTrue(register.isAvailable());
    }

    @Test
    void assignToFirstAvailableRegister_ShouldAssignWhenAvailable() {
        //Arrange
        Cashier cashier = employeeManager.addCashier("Viktor", new BigDecimal("1200"));
        CashRegister register = employeeManager.addCashRegistry();

        //Act
        boolean result = employeeManager.assignToFirstAvailableRegister(cashier);

        //Assert
        assertTrue(result);
        assertFalse(register.isAvailable());
    }

    @Test
    void assignToFirstAvailableRegister_ShouldFailWhenNoAvailableRegister() {
        //Arrange
        Cashier cashier = employeeManager.addCashier("Viktor", new BigDecimal("1200"));

        //Act
        boolean result = employeeManager.assignToFirstAvailableRegister(cashier);

        //Assert
        assertFalse(result);
    }

    @Test
    void unassignWorkerFromCashRegistry_ShouldUnassignFromCashRegistry() {
        //Arrange
        Cashier cashier = employeeManager.addCashier("Viktor", new BigDecimal("1200"));
        CashRegister register = employeeManager.addCashRegistry();

        //Act & Assert
        employeeManager.assignToFirstAvailableRegister(cashier);
        assertFalse(register.isAvailable());

        employeeManager.unassignWorkerFromCashRegistry(0);
        assertTrue(register.isAvailable());
    }

    @Test
    void unassignWorkerFromCashRegistry_ShouldThrowForInvalidIndex() {
        //Assert
        assertThrows(IndexOutOfBoundsException.class, () -> employeeManager.unassignWorkerFromCashRegistry(0));
    }
}