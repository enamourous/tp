package seedu.address.model.payment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 * Immutable record of a single payment.
 * Contains the Amount, the payment date, optional remarks, and the recordedAt timestamp.
 */
public final class Payment {
    private final Amount amount;
    private final LocalDate date;
    private final String remarks;
    private final LocalDateTime recordedAt;

    /**
     * Create a payment with no remarks. recordedAt defaults to now.
     */
    public Payment(Amount amount, LocalDate date) {
        this(amount, date, null, LocalDateTime.now());
    }

    /**
     * Create a payment with remarks. recordedAt defaults to now.
     */
    public Payment(Amount amount, LocalDate date, String remarks) {
        this(amount, date, remarks, LocalDateTime.now());
    }

    /**
     * Full constructor with explicit recordedAt.
     */
    public Payment(Amount amount, LocalDate date, String remarks, LocalDateTime recordedAt) {
        this.amount = Objects.requireNonNull(amount, "amount");
        this.date = Objects.requireNonNull(date, "date");
        this.remarks = tidy(remarks);
        this.recordedAt = Objects.requireNonNull(recordedAt, "recordedAt");
    }

    public Amount getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getRemarks() {
        return remarks;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }

    /**
     * String view such as "2025-03-12 | 23.50 | taxi home".
     */
    @Override
    public String toString() {
        String r = (remarks == null || remarks.isEmpty()) ? "" : (" | " + remarks);
        return date + " | $" + amount.toString() + r;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Payment)) {
            return false;
        }
        Payment p = (Payment) o;
        boolean sameRemarks = (this.remarks == null && p.remarks == null)
            || (this.remarks != null && this.remarks.equals(p.remarks));
        return this.amount.equals(p.amount)
            && this.date.equals(p.date)
            && sameRemarks
            && this.recordedAt.equals(p.recordedAt);
    }

    @Override
    public int hashCode() {
        int h = 17;
        h = 31 * h + amount.hashCode();
        h = 31 * h + date.hashCode();
        h = 31 * h + (remarks == null ? 0 : remarks.hashCode());
        h = 31 * h + recordedAt.hashCode();
        return h;
    }

    // ---------- helpers ----------

    private static String tidy(String s) {
        if (s == null) {
            return null;
        }
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    /**
     * Flexible parser that accepts both yyyy-MM-dd and yyyy-M-d formats.
     */
    public static LocalDate parseFlexibleDate(String dateStr) {
        Objects.requireNonNull(dateStr, "dateStr");
        String trimmed = dateStr.trim();
        LocalDate parsedDate;

        try {
            // Try strict yyyy-MM-dd first
            parsedDate = LocalDate.parse(trimmed, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            // Fallback to lenient yyyy-M-d
            DateTimeFormatter fallback = DateTimeFormatter.ofPattern("yyyy-M-d");
            parsedDate = LocalDate.parse(trimmed, fallback);
        }

        // Reject future dates
        if (parsedDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date cannot be in the future.");
        }

        return parsedDate;
    }

}
