package services;

import domain.store.CashRegister;
import domain.store.Cashier;
import org.junit.jupiter.api.Test;
import services.EmployeeManager;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeManagerTest {

    @Test
    void addCashier_ShouldAddAndReturnCashier() {
        EmployeeManager manager = new EmployeeManager();
        Cashier cashier = manager.addCashier("Anna", new BigDecimal("1000"));

        List<Cashier> cashiers = manager.getAllCashiers();
        assertTrue(cashiers.contains(cashier));
        assertEquals("Anna", cashier.getName());
        assertEquals(new BigDecimal("1000"), cashier.getSalary());
    }

    @Test
    void addCashRegistry_ShouldAddAndReturnRegister() {
        EmployeeManager manager = new EmployeeManager();
        CashRegister register = manager.addCashRegistry();

        assertTrue(register.isAvailable());
    }

    @Test
    void assignToFirstAvailableRegister_ShouldAssignCashier() {
        EmployeeManager manager = new EmployeeManager();
        Cashier cashier = manager.addCashier("Bob", new BigDecimal("1200"));
        CashRegister register = manager.addCashRegistry();

        boolean assigned = manager.assignToFirstAvailableRegister(cashier);

        assertTrue(assigned);
        assertFalse(register.isAvailable());
        assertEquals(cashier, register.getAssignedCashier());
    }

    @Test
    void assignToFirstAvailableRegister_ShouldReturnFalse_WhenNoAvailableRegister() {
        EmployeeManager manager = new EmployeeManager();
        Cashier cashier = manager.addCashier("Bob", new BigDecimal("1200"));

        boolean assigned = manager.assignToFirstAvailableRegister(cashier);

        assertFalse(assigned);
    }

    @Test
    void unassignWorkerFromCashRegistry_ShouldUnassign() {
        EmployeeManager manager = new EmployeeManager();
        Cashier cashier = manager.addCashier("Anna", new BigDecimal("1000"));
        manager.addCashRegistry();
        manager.assignToFirstAvailableRegister(cashier);

        manager.unassignWorkerFromCashRegistry(0);

        Cashier cashier2 = manager.addCashier("Bob", new BigDecimal("1200"));
        boolean assigned = manager.assignToFirstAvailableRegister(cashier2);
        assertTrue(assigned);
    }

    @Test
    void unassignWorkerFromCashRegistry_ShouldThrow_WhenIndexInvalid() {
        EmployeeManager manager = new EmployeeManager();
        manager.addCashRegistry();

        assertThrows(IndexOutOfBoundsException.class, () -> manager.unassignWorkerFromCashRegistry(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> manager.unassignWorkerFromCashRegistry(1));
    }

    @Test
    void getAllCashiers_ShouldReturnCopy() {
        EmployeeManager manager = new EmployeeManager();
        Cashier cashier = manager.addCashier("Anna", new BigDecimal("1000"));

        List<Cashier> cashiers = manager.getAllCashiers();
        cashiers.clear();

        assertFalse(manager.getAllCashiers().isEmpty());
    }
}
