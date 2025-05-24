package services;

import domain.store.CashRegister;
import domain.store.Cashier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeManagerTest {

    private EmployeeManager employeeManager;

    @BeforeEach
    void setUp() {
        employeeManager = new EmployeeManager();
    }

    @Test
    void addCashier() {
        Cashier cashier = employeeManager.addCashier("Viktor", new BigDecimal("1200"));
        List<Cashier> cashiers = employeeManager.getAllCashiers();

        assertEquals(1, cashiers.size());
        assertEquals("Viktor", cashiers.get(0).getName());
        assertEquals(new BigDecimal("1200"), cashiers.get(0).getSalary());
        assertSame(cashier, cashiers.get(0));
    }

    @Test
    void addCashRegister() {
        CashRegister register = employeeManager.addCashRegistry();

        assertNotNull(register);
        assertTrue(register.isAvailable());
    }

    @Test
    void assignToFirstAvailableRegister_ShouldAssignWhenAvailable() {
        Cashier cashier = employeeManager.addCashier("Viktor", new BigDecimal("1200"));
        CashRegister register = employeeManager.addCashRegistry();

        boolean result = employeeManager.assignToFirstAvailableRegister(cashier);

        assertTrue(result);
        assertFalse(register.isAvailable());
    }

    @Test
    void assignToFirstAvailableRegister_ShouldFailWhenNoAvailableRegister() {
        Cashier cashier = employeeManager.addCashier("Viktor", new BigDecimal("1200"));

        // No register added
        boolean result = employeeManager.assignToFirstAvailableRegister(cashier);

        assertFalse(result);
    }

    @Test
    void unassignWorkerFromCashRegistry() {
        Cashier cashier = employeeManager.addCashier("Viktor", new BigDecimal("1200"));
        CashRegister register = employeeManager.addCashRegistry();

        employeeManager.assignToFirstAvailableRegister(cashier);
        assertFalse(register.isAvailable());

        employeeManager.unassignWorkerFromCashRegistry(0);
        assertTrue(register.isAvailable());
    }

    @Test
    void unassignWorkerFromCashRegistry_ShouldThrowForInvalidIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> employeeManager.unassignWorkerFromCashRegistry(0));
    }
}