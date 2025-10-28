package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import seedu.address.model.person.exceptions.InvalidMatriculationNumberException;

/**
 * Represents a Person's matriculation number in the address book.
 * Guarantees: immutable; always valid as declared in {@link #isValidMatriculationNumber(String)}.
 */
public class MatriculationNumber {

    public static final String MESSAGE_CONSTRAINTS =
        "Matriculation numbers must be exactly 9 characters long, "
            + "start with 'A', end with an alphabet, "
            + "and contain digits in between (e.g. A1234567X).";

    /**
     * Format:
     * - First char: 'A'
     * - Next 8 chars: digits 0–9
     * - Last char: alphabet (A–Z)
     * Total length: 9.
     */
    public static final String VALIDATION_REGEX = "A\\d{7}[A-Z]";

    public final String value;

    /**
     * Constructs a {@code MatriculationNumber}.
     *
     * @param input A valid matriculation number.
     */
    public MatriculationNumber(String input) {
        requireNonNull(input);

        // Convert to uppercase automatically
        String normalized = input.toUpperCase();

        if (!isValidMatriculationNumber(normalized)) {
            throw new InvalidMatriculationNumberException(normalized);
        }

        value = normalized;
    }

    /**
     * Returns true if the given string is a valid matriculation number.
     */
    public static boolean isValidMatriculationNumber(String test) {
        return test != null && test.toUpperCase().matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof MatriculationNumber otherMatriculationNumber)) {
            return false;
        }

        return value.equals(otherMatriculationNumber.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
