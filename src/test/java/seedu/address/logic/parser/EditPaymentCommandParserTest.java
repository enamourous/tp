package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditPaymentCommand;
import seedu.address.logic.commands.EditPaymentCommand.EditPaymentDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Expected CLI: PERSON_INDEX p/PAYMENT_INDEX [a/AMOUNT] [d/DATE] [r/REMARKS]
 */
public class EditPaymentCommandParserTest {

    private final EditPaymentCommandParser parser = new EditPaymentCommandParser();

    @Test
    public void parse_minimalFields_success() throws Exception {
        String input = "1 p/2 r/late fee";
        EditPaymentCommand actual = parser.parse(input);

        EditPaymentDescriptor d = new EditPaymentDescriptor();
        d.setRemarks("late fee");
        EditPaymentCommand expected = new EditPaymentCommand(Index.fromOneBased(1), 2, d);

        assertEquals(expected, actual);
    }

    @Test
    public void parse_missingPaymentIndex_failure() {
        assertThrows(ParseException.class, () -> parser.parse("1 a/10.50"));
    }

    @Test
    public void parse_invalidPersonIndex_failure() {
        assertThrows(ParseException.class, () -> parser.parse("0 p/1 r/x"));
        assertThrows(ParseException.class, () -> parser.parse("-3 p/1 r/x"));
    }

    @Test
    public void parse_noEditableFields_failure() {
        assertThrows(ParseException.class, () -> parser.parse("1 p/1"));
    }
}
