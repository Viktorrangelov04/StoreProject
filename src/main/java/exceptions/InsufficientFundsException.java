package exceptions;

import java.math.BigDecimal;

public class InsufficientFundsException extends Exception {
    private final BigDecimal required;
    private final BigDecimal available;

    public InsufficientFundsException(BigDecimal required, BigDecimal available) {
        super("Insufficient funds. Required: " + required + " BGN, Available: " + available + " BGN");
        this.required = required;
        this.available = available;
    }

    public BigDecimal getRequired() {
        return required;
    }

    public BigDecimal getAvailable() {
        return available;
    }

    public BigDecimal getShortfall() {
        return required.subtract(available);
    }
}

