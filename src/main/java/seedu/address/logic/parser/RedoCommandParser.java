package seedu.address.logic.parser;

import seedu.address.logic.commands.RedoCommand;

/**
 * Parses input arguments and creates a new {@code RedoCommand} object.
 */
public class RedoCommandParser implements Parser<RedoCommand> {
    @Override
    public RedoCommand parse(String args) {
        return new RedoCommand(); // no arguments
    }
}

