package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;

/**
 * Unit tests for {@link ViewPaymentCommand} (equality and basic semantics).
 */
public class ViewPaymentCommandTest {

    @Test
    public void equals_sameValues_returnsTrue() {
        ViewPaymentCommand a = new ViewPaymentCommand(Index.fromOneBased(1));
        ViewPaymentCommand b = new ViewPaymentCommand(Index.fromOneBased(1));
        assertEquals(a, b);
    }

    @Test
    public void equals_differentIndex_returnsFalse() {
        ViewPaymentCommand a = new ViewPaymentCommand(Index.fromOneBased(1));
        ViewPaymentCommand b = new ViewPaymentCommand(Index.fromOneBased(2));
        assertNotEquals(a, b);
    }

    @Test
    public void equals_nullOrDifferentType_returnsFalse() {
        ViewPaymentCommand a = new ViewPaymentCommand(Index.fromOneBased(1));
        assertNotEquals(a, null);
        assertNotEquals(a, "not-a-command");
    }
}
