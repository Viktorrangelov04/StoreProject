package services;

import domain.store.Cashier;

import java.math.BigDecimal;
import java.util.List;

public class FinancialManager {
    private BigDecimal storeRevenue = BigDecimal.ZERO;
    private BigDecimal totalDeliveryCost = BigDecimal.ZERO;

    public void increaseTotalDeliveryCost(BigDecimal currentDeliveryCost) {
        this.totalDeliveryCost = this.totalDeliveryCost.add(currentDeliveryCost);
    }

    public void increaseStoreRevenue(BigDecimal currentRevenue) {
        this.storeRevenue = this.storeRevenue.add(currentRevenue);
    }

    public BigDecimal calculateProfit(List<Cashier> cashiers) {
        BigDecimal totalSalaries = cashiers.stream()
                .map(Cashier::getSalary)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return storeRevenue.subtract(totalDeliveryCost.add(totalSalaries));
    }
}
