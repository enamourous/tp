package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.Messages;
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
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, DeletePaymentCommand.MESSAGE_USAGE),
                e.getMessage()
        );
    }

}
