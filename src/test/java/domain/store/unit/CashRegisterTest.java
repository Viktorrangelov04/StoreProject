package domain.store.unit;

import domain.store.CashRegister;
import domain.store.Cashier;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class CashRegisterTest {
    @Test
    void initialState_ShouldBeAvailableAndUnassigned() {
        CashRegister register = new CashRegister();
        assertTrue(register.isAvailable());
        assertNull(register.getAssignedCashier());
    }

    @Test
    void assign_ShouldAssignCashierAndSetUnavailable() {
        CashRegister register = new CashRegister();
        Cashier cashier = new Cashier("Anna", new BigDecimal("1000"));
        register.assign(cashier);

        assertFalse(register.isAvailable());
        assertEquals(cashier, register.getAssignedCashier());
    }

    @Test
    void assign_WhenAlreadyAssigned_ShouldThrow() {
        CashRegister register = new CashRegister();
        Cashier cashier1 = new Cashier("Anna", new BigDecimal("1000"));
        Cashier cashier2 = new Cashier("Bob", new BigDecimal("1200"));
        register.assign(cashier1);

        assertThrows(IllegalStateException.class, () -> register.assign(cashier2));
    }

    @Test
    void unassign_ShouldClearCashierAndSetAvailable() {
        CashRegister register = new CashRegister();
        Cashier cashier = new Cashier("Anna", new BigDecimal("1000"));
        register.assign(cashier);

        register.unassign();

        assertTrue(register.isAvailable());
        assertNull(register.getAssignedCashier());
    }
}
