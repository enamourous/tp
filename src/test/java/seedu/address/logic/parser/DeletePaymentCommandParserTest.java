package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.DeletePaymentCommand.MESSAGE_USAGE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeletePaymentCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class DeletePaymentCommandParserTest {

    private final DeletePaymentCommandParser parser = new DeletePaymentCommandParser();

    @Test
    public void parse_validArgs_returnsDeletePaymentCommand() throws Exception {
        // Example: "1 p/1" → delete payment #1 from person #1
        DeletePaymentCommand expectedCommand =
                new DeletePaymentCommand(Index.fromOneBased(1),
                        java.util.List.of(Index.fromOneBased(1)));

        assertEquals(expectedCommand, parser.parse("1 p/1"));
    }

    @Test
    public void parse_multiplePaymentIndexes_returnsDeletePaymentCommand() throws Exception {
        // Example: "1 p/1,2,3" → delete payments #1, #2, #3 from person #1
        DeletePaymentCommand expectedCommand =
                new DeletePaymentCommand(Index.fromOneBased(1),
                        java.util.List.of(
                                Index.fromOneBased(1),
                                Index.fromOneBased(2),
                                Index.fromOneBased(3)
                        ));

        assertEquals(expectedCommand, parser.parse("1 p/1,2,3"));
    }

    @Test
    public void parse_missingPaymentIndex_throwsParseException() {
        ParseException e = assertThrows(ParseException.class, () -> parser.parse("1"));
        assertEquals(
                "Missing payment index after 'p/'. Example: p/1,2,3",
                e.getMessage()
        );
    }

    @Test
    public void parse_invalidPersonIndex_throwsParseException() {
        ParseException e = assertThrows(ParseException.class, () -> parser.parse("a p/1"));
        assertEquals(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE),
                e.getMessage()
        );
    }

    @Test
    public void parse_duplicatePaymentIndexes_failure() {
        String input = "2 p/1,2,2";
        assertParseFailure(parser, input,
                DeletePaymentCommandParser.MESSAGE_DUPLICATE_PAYMENT_INDEX);
    }

    @Test
    public void parse_emptyPaymentIndex_failure() {
        // Case: empty token between commas (e.g. "p/1,,2")
        assertParseFailure(parser, "1 p/1,,2",
                "Empty payment indexes are not allowed. Remove stray commas/spaces. Example: p/1,2,3");

        // Case: trailing comma (e.g. "p/1,2,")
        assertParseFailure(parser, "1 p/1,2,",
                "Empty payment indexes are not allowed. Remove stray commas/spaces. Example: p/1,2,3");

        // Case: extra spaces (e.g. "p/1, ,2")
        assertParseFailure(parser, "1 p/1, ,2",
                "Empty payment indexes are not allowed. Remove stray commas/spaces. Example: p/1,2,3");
    }
}
