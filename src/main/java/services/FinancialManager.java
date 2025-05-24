package services;

import domain.store.Cashier;

import java.math.BigDecimal;
import java.util.List;

public class FinancialManager {
    private BigDecimal storeRevenue;
    private BigDecimal totalDeliveryCost;

    public FinancialManager() {
        this.storeRevenue = BigDecimal.ZERO;
        this.totalDeliveryCost = BigDecimal.ZERO;
    }

    public BigDecimal getStoreRevenue() {
        return storeRevenue;
    }

    public BigDecimal getTotalDeliveryCost() {
        return totalDeliveryCost;
    }

    public void addDeliveryCost(BigDecimal cost) {
        this.totalDeliveryCost = this.totalDeliveryCost.add(cost);
    }

    public void addRevenue(BigDecimal amount) {
        this.storeRevenue = this.storeRevenue.add(amount);
    }

    public BigDecimal calculateProfit(List<Cashier> cashiers) {
        BigDecimal totalSalaries = cashiers.stream()
                .map(Cashier::getSalary)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return storeRevenue.subtract(totalDeliveryCost.add(totalSalaries));
    }
}
