// src/test/java/seedu/address/logic/parser/EditPaymentCommandParserEdgeTest.java
package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;

public class EditPaymentCommandParserEdgeTest {

    private final EditPaymentCommandParser parser = new EditPaymentCommandParser();

    @Test
    public void parse_missingPaymentIndex_failure() {
        assertThrows(ParseException.class, () -> parser.parse("1 a/10.50"));
    }

    @Test
    public void parse_noEditableFields_failure() {
        assertThrows(ParseException.class, () -> parser.parse("1 p/1"));
    }
}
