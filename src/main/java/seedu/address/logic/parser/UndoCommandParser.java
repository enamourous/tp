package seedu.address.logic.parser;

import seedu.address.logic.commands.UndoCommand;

/**
 * Parses input arguments and creates a new {@code UndoCommand} object.
 */
public class UndoCommandParser implements Parser<UndoCommand> {
    @Override
    public UndoCommand parse(String args) {
        return new UndoCommand();
    }
}
