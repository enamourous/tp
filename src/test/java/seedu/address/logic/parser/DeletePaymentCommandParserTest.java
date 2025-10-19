package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeletePaymentCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class DeletePaymentCommandParserTest {

    private final DeletePaymentCommandParser parser = new DeletePaymentCommandParser();

    @Test
    public void parse_validArgs_returnsDeletePaymentCommand() throws Exception {
        // Example: "1 p/1" → delete payment #1 from person #1
        DeletePaymentCommand command = parser.parse("1 p/1");
        assertEquals(new DeletePaymentCommand(List.of(Index.fromOneBased(1)), Index.fromOneBased(1)), command);
    }

    @Test
    public void parse_multiplePersons_returnsDeletePaymentCommand() throws Exception {
        // Example: "1,2 p/1" → delete payment #1 from persons #1 and #2
        DeletePaymentCommand command = parser.parse("1,2 p/1");
        assertEquals(new DeletePaymentCommand(
                List.of(Index.fromOneBased(1), Index.fromOneBased(2)),
                Index.fromOneBased(1)), command);
    }

    @Test
    public void parse_missingPaymentIndex_throwsParseException() {
        try {
            parser.parse("1");
        } catch (ParseException e) {
            assertEquals(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeletePaymentCommand.MESSAGE_USAGE),
                    e.getMessage()
            );
        }
    }

    @Test
    public void parse_invalidPersonIndex_throwsParseException() {
        try {
            parser.parse("a p/1");
        } catch (ParseException e) {
            assertEquals(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeletePaymentCommand.MESSAGE_USAGE),
                    e.getMessage()
            );
        }
    }
}
