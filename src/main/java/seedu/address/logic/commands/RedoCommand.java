package seedu.address.logic.commands;

import seedu.address.model.Model;

/**
 * Performs a redo on the most recent undo action
 */
public class RedoCommand extends Command {
    public static final String COMMAND_WORD = "redo";
    public static final String MESSAGE_SUCCESS = "Redid the last undone change.";
    public static final String MESSAGE_NOTHING = "Nothing to redo.";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Redoes the last undone change in the address book.\n"
            + "Format: " + COMMAND_WORD + "\n"
            + "Example: " + COMMAND_WORD;


    @Override
    public CommandResult execute(Model model) {
        if (!model.canRedo()) {
            return new CommandResult(MESSAGE_NOTHING);
        }
        model.redo();
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        return other == this || other instanceof RedoCommand;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
