package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditPaymentCommand.EditPaymentDescriptor;

/**
 * Unit tests for {@link EditPaymentCommand} (equality of command instances).
 */
public class EditPaymentCommandTest {

    @Test
    public void equals_sameValues_returnsTrue() {
        EditPaymentDescriptor d1 = new EditPaymentDescriptor();
        d1.setRemarks("late fee");

        EditPaymentDescriptor d2 = new EditPaymentDescriptor();
        d2.setRemarks("late fee");

        EditPaymentCommand a = new EditPaymentCommand(Index.fromOneBased(1), 1, d1);
        EditPaymentCommand b = new EditPaymentCommand(Index.fromOneBased(1), 1, d2);
        assertEquals(a, b);
    }

    @Test
    public void equals_differentPersonIndex_returnsFalse() {
        EditPaymentDescriptor d = new EditPaymentDescriptor();
        d.setRemarks("x");
        EditPaymentCommand a = new EditPaymentCommand(Index.fromOneBased(1), 1, d);
        EditPaymentCommand b = new EditPaymentCommand(Index.fromOneBased(2), 1, d);
        assertNotEquals(a, b);
    }

    @Test
    public void equals_differentPaymentIndex_returnsFalse() {
        EditPaymentDescriptor d = new EditPaymentDescriptor();
        d.setRemarks("x");
        EditPaymentCommand a = new EditPaymentCommand(Index.fromOneBased(1), 1, d);
        EditPaymentCommand b = new EditPaymentCommand(Index.fromOneBased(1), 2, d);
        assertNotEquals(a, b);
    }

    @Test
    public void equals_differentDescriptor_returnsFalse() {
        EditPaymentDescriptor d1 = new EditPaymentDescriptor();
        d1.setRemarks("x");

        EditPaymentDescriptor d2 = new EditPaymentDescriptor();
        d2.setRemarks("y");

        EditPaymentCommand a = new EditPaymentCommand(Index.fromOneBased(1), 1, d1);
        EditPaymentCommand b = new EditPaymentCommand(Index.fromOneBased(1), 1, d2);
        assertNotEquals(a, b);
    }
}
