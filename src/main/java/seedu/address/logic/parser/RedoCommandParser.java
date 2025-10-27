package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new RedoCommand object
 */
public class RedoCommandParser implements Parser<RedoCommand> {
    @Override
    public RedoCommand parse(String args) throws ParseException {
        if (args == null || args.trim().isEmpty()) {
            return new RedoCommand();
        }
        throw new ParseException(String.format(
                MESSAGE_INVALID_COMMAND_FORMAT, RedoCommand.MESSAGE_USAGE));
    }
}
