// src/test/java/seedu/address/logic/parser/ViewPaymentCommandParserTest.java
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
        ViewPaymentCommand expected = new ViewPaymentCommand(Index.fromOneBased(1));
        ViewPaymentCommand actual = parser.parse("1");
        assertEquals(expected, actual);
    }

    @Test
    public void parseInvalidIndexZero_fails() {
        assertThrows(ParseException.class, () -> parser.parse("0"));
    }

    @Test
    public void parseInvalidIndexNegative_fails() {
        assertThrows(ParseException.class, () -> parser.parse("-5"));
    }

    @Test
    public void parseInvalidFormatNonInteger_fails() {
        assertThrows(ParseException.class, () -> parser.parse("one"));
        assertThrows(ParseException.class, () -> parser.parse("1 2"));
    }
}
