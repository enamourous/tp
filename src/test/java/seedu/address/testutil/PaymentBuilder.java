package seedu.address.testutil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

import seedu.address.model.payment.Amount;
import seedu.address.model.payment.Payment;

/**
 * Test helper to build {@link Payment} instances.
 *
 * <p>It supports Payment constructors of either
 * (Amount, LocalDate, String) or (Amount, LocalDate, String, Instant),
 * and supports {@link Amount} creation via:
 * <ul>
 *   <li>new Amount(BigDecimal)</li>
 *   <li>new Amount(String)</li>
 *   <li>Amount.of(String)</li>
 *   <li>Amount.valueOf(String)</li>
 *   <li>Amount.parse(String)</li>
 * </ul>
 */
public class PaymentBuilder {

    private String amount = "10.00";
    private String date = "2025-01-01";
    private String remarks = "";
    private Instant recordedAt = Instant.parse("2025-01-01T00:00:00Z");

    /** Sets the amount string for this builder. */
    public PaymentBuilder withAmount(String amount) {
        this.amount = amount;
        return this;
    }

    /** Sets the transaction date (YYYY-MM-DD) for this builder. */
    public PaymentBuilder withDate(String yyyyMmDd) {
        this.date = yyyyMmDd;
        return this;
    }

    /** Sets the remarks for this builder. */
    public PaymentBuilder withRemarks(String remarks) {
        this.remarks = remarks;
        return this;
    }

    /** Sets the recorded-at timestamp for this builder (used if a 4-arg ctor exists). */
    public PaymentBuilder withRecordedAt(Instant recordedAt) {
        this.recordedAt = recordedAt;
        return this;
    }

    /** Builds and returns the {@link Payment}. */
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
                // Fall through to 3-arg constructor.
            }

            Constructor<Payment> c3 = Payment.class.getConstructor(
                    Amount.class, LocalDate.class, String.class);
            return c3.newInstance(amt, dt, remarks);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Unable to construct Payment via reflection", e);
        }
    }

    private Amount buildAmountObj() {
        ReflectiveOperationException last = null;

        // Try Amount(BigDecimal)
        try {
            Constructor<Amount> c = Amount.class.getConstructor(BigDecimal.class);
            return c.newInstance(new BigDecimal(this.amount));
        } catch (ReflectiveOperationException e) {
            last = e;
        }

        // Try Amount(String)
        try {
            Constructor<Amount> c = Amount.class.getConstructor(String.class);
            return c.newInstance(this.amount);
        } catch (ReflectiveOperationException e) {
            last = e;
        }

        // Try static factories: of / valueOf / parse
        try {
            Method m = Amount.class.getMethod("of", String.class);
            return (Amount) m.invoke(null, this.amount);
        } catch (ReflectiveOperationException e) {
            last = e;
        }

        try {
            Method m = Amount.class.getMethod("valueOf", String.class);
            return (Amount) m.invoke(null, this.amount);
        } catch (ReflectiveOperationException e) {
            last = e;
        }

        try {
            Method m = Amount.class.getMethod("parse", String.class);
            return (Amount) m.invoke(null, this.amount);
        } catch (ReflectiveOperationException e) {
            last = e;
        }

        throw new RuntimeException("Unable to construct Amount from: " + this.amount, last);
    }
}
