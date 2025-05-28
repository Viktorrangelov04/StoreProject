package domain.store.unit;

import domain.product.Category;
import domain.product.Product;
import domain.store.Cashier;
import domain.store.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pricing.ExpiryDiscountStrategy;
import pricing.MarkupStrategy;
import pricing.PricingStrategy;
import services.EmployeeManager;
import services.FinancialManager;
import services.InventoryManager;
import services.PurchaseManager;
import storage.ReceiptStorage;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoreTest {

    @Mock
    InventoryManager inventoryManager;
    @Mock
    EmployeeManager employeeManager;
    @Mock
    FinancialManager financialManager;
    @Mock
    PricingStrategy pricingStrategy;
    @Mock
    ExpiryDiscountStrategy expiryDiscountStrategy;
    @Mock
    ReceiptStorage receiptStorage;
    @Mock
    PurchaseManager purchaseManager;

    Store store;

    @BeforeEach
    void setUp() {
        store = new Store(
                "Test Store",
                inventoryManager,
                employeeManager,
                financialManager,
                pricingStrategy,
                expiryDiscountStrategy,
                receiptStorage,
                purchaseManager
        );
    }

    @Test
    void addStock_shouldCalculateSellingPriceAndAddStockAndDeliveryCost() {
        Product product = new Product("Apple", Category.Food);
        BigDecimal deliveryPrice = new BigDecimal("2.00");
        int quantity = 10;
        LocalDate expiry = LocalDate.now().plusDays(5);

        when(pricingStrategy.calculateSellingPrice(any(), any()))
                .thenReturn(new BigDecimal("3.00"));

        store.addStock(product, quantity, deliveryPrice, expiry);

        verify(pricingStrategy).calculateSellingPrice(deliveryPrice, product.getCategory());
        verify(financialManager).addDeliveryCost(new BigDecimal("20.00"));
        verify(inventoryManager).addStock(argThat(item ->
                item.getProduct().equals(product) &&
                        item.getDeliveryPrice().equals(deliveryPrice) &&
                        item.getQuantity() == quantity &&
                        item.getExpiryDate().equals(expiry) &&
                        item.getSellingPrice().equals(new BigDecimal("3.00"))
        ));
    }

    @Test
    void setMarkup_shouldCallMarkupStrategy() {
        Category category = Category.Food;
        BigDecimal newPercent = new BigDecimal("0.15");
        MarkupStrategy markupStrategy = mock(MarkupStrategy.class);

        Store markupStore = new Store(
                "Test Store",
                inventoryManager,
                employeeManager,
                financialManager,
                markupStrategy,
                expiryDiscountStrategy,
                receiptStorage,
                purchaseManager
        );
        markupStore.setMarkup(category, newPercent);

        verify(markupStrategy).setMarkup(category, newPercent);
    }

    @Test
    void setMarkup_shouldUseMarkupStrategy() {
        Category category = Category.Food;
        BigDecimal newPercent = new BigDecimal("0.15");
        MarkupStrategy markupStrategy = mock(MarkupStrategy.class);

        Store markupStore = new Store(
                "Test Store",
                inventoryManager,
                employeeManager,
                financialManager,
                markupStrategy,
                expiryDiscountStrategy,
                receiptStorage,
                purchaseManager
        );

        markupStore.setMarkup(category, newPercent);

        verify(markupStrategy).setMarkup(category, newPercent);
    }

    @Test
    void getMarkup_shouldUseMarkupStrategyAndReturnValue() {
        Category category = Category.Food;
        BigDecimal expectedMarkup = new BigDecimal("0.20");
        MarkupStrategy markupStrategy = mock(MarkupStrategy.class);
        when(markupStrategy.getMarkup(category)).thenReturn(expectedMarkup);

        Store markupStore = new Store(
                "Test Store",
                inventoryManager,
                employeeManager,
                financialManager,
                markupStrategy,
                expiryDiscountStrategy,
                receiptStorage,
                purchaseManager
        );

        BigDecimal result = markupStore.getMarkup(category);

        assertEquals(expectedMarkup, result);
        verify(markupStrategy).getMarkup(category);
    }

    @Test
    void getStoreRevenue_shouldUseFinancialManager() {
        BigDecimal expectedRevenue = new BigDecimal("1000.00");
        when(financialManager.getStoreRevenue()).thenReturn(expectedRevenue);

        BigDecimal result = store.getStoreRevenue();

        assertEquals(expectedRevenue, result);
        verify(financialManager).getStoreRevenue();
    }

    @Test
    void processPurchase_shouldUsePurchaseManager() throws Exception {
        Scanner scanner = mock(Scanner.class);
        BigDecimal clientMoney = new BigDecimal("50.00");
        Cashier cashier = mock(Cashier.class);

        store.processPurchase(scanner, clientMoney, cashier);

        verify(purchaseManager).processPurchase(scanner, clientMoney, cashier);
    }

}
