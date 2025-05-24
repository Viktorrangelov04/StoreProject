package exceptions;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class InsufficientFundsExceptionTest {

    @Test
    public void exception_ShouldContainCorrectMessageAndValues() {
        // Arrange
        BigDecimal required = new BigDecimal("50.00");
        BigDecimal available = new BigDecimal("30.00");

        // Act
        InsufficientFundsException ex = new InsufficientFundsException(required, available);

        // Assert
        assertEquals(required, ex.getRequired());
        assertEquals(available, ex.getAvailable());
        assertEquals(new BigDecimal("20.00"), ex.getShortfall());
        assertEquals("Insufficient funds. Required: 50.00 BGN, Available: 30.00 BGN", ex.getMessage());
    }

    @Test
    public void exception_ShouldBeThrowable() {
        // Arrange
        BigDecimal required = new BigDecimal("100");
        BigDecimal available = new BigDecimal("80");

        // Assert
        Exception exception = assertThrows(InsufficientFundsException.class, () -> {
            throw new InsufficientFundsException(required, available);
        });

        assertTrue(exception.getMessage().contains("Insufficient funds"));
    }
}