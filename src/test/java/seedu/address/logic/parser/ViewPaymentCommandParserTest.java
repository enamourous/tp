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
    public void parse_valid_success() throws Exception {
        ViewPaymentCommand cmd = parser.parse("1");
        assertEquals(new ViewPaymentCommand(Index.fromOneBased(1)), cmd);
    }

    @Test
    public void parse_invalidIndex_failure() {
        assertThrows(ParseException.class, () -> parser.parse(""));
        assertThrows(ParseException.class, () -> parser.parse("0"));
        assertThrows(ParseException.class, () -> parser.parse("-1"));
        assertThrows(ParseException.class, () -> parser.parse("abc"));
    }
}
