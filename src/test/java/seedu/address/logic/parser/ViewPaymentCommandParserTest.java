package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ViewPaymentCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class ViewPaymentCommandParserTest {

    private final ViewPaymentCommandParser parser = new ViewPaymentCommandParser();

    @Test
    public void parse_validIndex_success() throws Exception {
        ViewPaymentCommand cmd = parser.parse("1");
        assertEquals(new ViewPaymentCommand(Index.fromOneBased(1)), cmd);
    }

    @Test
    public void parse_zeroIndex_failure() {
        assertThrows(ParseException.class, () -> parser.parse("0"));
    }

    @Test
    public void parse_negativeIndex_failure() {
        assertThrows(ParseException.class, () -> parser.parse("-3"));
    }

    @Test
    public void parse_nonInteger_failure() {
        assertThrows(ParseException.class, () -> parser.parse("abc"));
        assertThrows(ParseException.class, () -> parser.parse("1.2"));
        assertThrows(ParseException.class, () -> parser.parse(""));
    }
}
