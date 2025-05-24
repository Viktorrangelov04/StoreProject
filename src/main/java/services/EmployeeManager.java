package services;

import domain.store.CashRegister;
import domain.store.Cashier;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class EmployeeManager {
    private final List<Cashier> cashiers = new ArrayList<>();
    private final List<CashRegister> cashRegisters = new ArrayList<>();

    public List<Cashier> getAllCashiers() {
        return new ArrayList<>(cashiers); // Return copy for safety
    }

    public Cashier addCashier(String name, BigDecimal salary) {
        Cashier cashier = new Cashier(name, salary);
        cashiers.add(cashier);
        return cashier;
    }

    public CashRegister addCashRegistry() {
        CashRegister registry = new CashRegister();
        cashRegisters.add(registry);
        return registry;
    }

    public boolean assignToFirstAvailableRegister(Cashier cashier) {
        for (CashRegister registry : cashRegisters) {
            if (registry.isAvailable()) {
                registry.assign(cashier);
                System.out.println("Cashier " + cashier.getName() + " assigned to a register.");
                return true;
            }
        }
        System.out.println("No available registers for cashier " + cashier.getName());
        return false;
    }

    public void unassignWorkerFromCashRegistry(int index) {
        if (index < 0 || index >= cashRegisters.size()) {
            throw new IndexOutOfBoundsException("Invalid registry index");
        }
        cashRegisters.get(index).unassign();
    }

}
