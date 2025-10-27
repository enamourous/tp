package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses any input and creates a new {@code ExitCommand} object.
 */
public class ExitCommandParser implements Parser<ExitCommand> {

    @Override
    public ExitCommand parse(String args) throws ParseException {
        // exit must have no trailing args (allow whitespace)
        if (args != null && !args.trim().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExitCommand.MESSAGE_USAGE));
        }
        return new ExitCommand();
    }
}
