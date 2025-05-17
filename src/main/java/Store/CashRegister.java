package Store;

public class CashRegister {
    private boolean available = true;
    private Cashier assignedCashier;

    public boolean isAvailable(){
        return available;
    }
    public void assign(Cashier cashier){
        if(!available){
            throw new IllegalStateException("This registry is already assigned.");
        }
        available = false;
    }
    public void unassign(){
        available = true;
    }
    public Cashier getAssignedCashier(){
        return assignedCashier;
    }
}
