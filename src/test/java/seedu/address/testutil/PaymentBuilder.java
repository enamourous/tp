package seedu.address.testutil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

import seedu.address.model.payment.Amount;
import seedu.address.model.payment.Payment;

/**
 * Builds Payment instances for tests. Works whether your Payment has a
 * (Amount, LocalDate, String) or (Amount, LocalDate, String, Instant) ctor,
 * and whether Amount takes BigDecimal or String (or has static of/parse/valueOf).
 */
public class PaymentBuilder {
    private String amount = "10.00";
    private String date = "2025-01-01";
    private String remarks = "";
    private Instant recordedAt = Instant.parse("2025-01-01T00:00:00Z");

    public PaymentBuilder withAmount(String amount) {
        this.amount = amount;
        return this;
    }

    public PaymentBuilder withDate(String yyyyMmDd) {
        this.date = yyyyMmDd;
        return this;
    }

    public PaymentBuilder withRemarks(String remarks) {
        this.remarks = remarks;
        return this;
    }

    public PaymentBuilder withRecordedAt(Instant recordedAt) {
        this.recordedAt = recordedAt;
        return this;
    }

    private Amount buildAmountObj() {
        // Try Amount(BigDecimal)
        try {
            Constructor<Amount> c = Amount.class.getConstructor(BigDecimal.class);
            return c.newInstance(new BigDecimal(this.amount));
        } catch (ReflectiveOperationException ignored) { }

        // Try Amount(String)
        try {
            Constructor<Amount> c = Amount.class.getConstructor(String.class);
            return c.newInstance(this.amount);
        } catch (ReflectiveOperationException ignored) { }

        // Try static factories: of / valueOf / parse
        try {
            Method m = Amount.class.getMethod("of", String.class);
            return (Amount) m.invoke(null, this.amount);
        } catch (ReflectiveOperationException ignored) { }

        try {
            Method m = Amount.class.getMethod("valueOf", String.class);
            return (Amount) m.invoke(null, this.amount);
        } catch (ReflectiveOperationException ignored) { }

        try {
            Method m = Amount.class.getMethod("parse", String.class);
            return (Amount) m.invoke(null, this.amount);
        } catch (ReflectiveOperationException ignored) { }

        throw new RuntimeException("Unable to construct Amount from: " + this.amount);
    }

    public Payment build() {
        try {
            Amount amt = buildAmountObj();
            LocalDate dt = LocalDate.parse(this.date);

            // Prefer 4-arg ctor if present
            try {
                Constructor<Payment> c4 = Payment.class.getConstructor(
                        Amount.class, LocalDate.class, String.class, Instant.class);
                return c4.newInstance(amt, dt, remarks, recordedAt);
            } catch (NoSuchMethodException e) {
                // fall through
            }

            // Fallback to 3-arg ctor
            Constructor<Payment> c3 = Payment.class.getConstructor(
                    Amount.class, LocalDate.class, String.class);
            return c3.newInstance(amt, dt, remarks);

        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Unable to construct Payment via reflection", e);
        }
    }
}
