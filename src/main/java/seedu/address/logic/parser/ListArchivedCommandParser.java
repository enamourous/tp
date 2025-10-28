package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.ListArchivedCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ListArchivedCommand object
 */
public class ListArchivedCommandParser implements Parser<ListArchivedCommand> {
    @Override
    public ListArchivedCommand parse(String args) throws ParseException {
        if (args == null || args.trim().isEmpty()) {
            return new ListArchivedCommand();
        }
        throw new ParseException(String.format(
                MESSAGE_INVALID_COMMAND_FORMAT, ListArchivedCommand.MESSAGE_USAGE));
    }
}
