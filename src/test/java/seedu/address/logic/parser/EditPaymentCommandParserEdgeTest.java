package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;

public class EditPaymentCommandParserEdgeTest {
    private final EditPaymentCommandParser parser = new EditPaymentCommandParser();

    @Test
    public void parse_invalidAmount_failure() {
        assertThrows(ParseException.class, () -> parser.parse("1 p/1 a/-3.00 r/x"));
        assertThrows(ParseException.class, () -> parser.parse("1 p/1 a/12.345 r/x"));
        assertThrows(ParseException.class, () -> parser.parse("1 p/1 a/abc r/x"));
    }

    @Test
    public void parse_invalidDate_failure() {
        assertThrows(ParseException.class, () -> parser.parse("1 p/1 d/2025-13-01 r/x"));
        assertThrows(ParseException.class, () -> parser.parse("1 p/1 d/2025-02-30 r/x"));
        assertThrows(ParseException.class, () -> parser.parse("1 p/1 d/not-a-date r/x"));
    }

    @Test
    public void parse_whitespaceRobust_okOrClearFailure() throws Exception {
        try { parser.parse("   1   p/1   r/test   "); } catch (ParseException ignored) { }
        try { parser.parse("\n1\np/1\nr/test\n"); } catch (ParseException ignored) { }
    }
}
