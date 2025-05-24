package domain.store;

public class CashRegister {
    private boolean available = true;
    private Cashier assignedCashier=null;

    public boolean isAvailable(){
        return available;
    }
    public void assign(Cashier cashier){
        if(!available){
            throw new IllegalStateException("This registry is already assigned.");
        }
        assignedCashier = cashier;
        available = false;
    }
    public void unassign(){
        assignedCashier = null;
        available = true;
    }
    public Cashier getAssignedCashier(){
        return assignedCashier;
    }
}
