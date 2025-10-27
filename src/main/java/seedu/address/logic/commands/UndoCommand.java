package seedu.address.logic.commands;

import seedu.address.model.Model;

/**
 * Performs undo on the most recent action
 */
public class UndoCommand extends Command {
    public static final String COMMAND_WORD = "undo";
    public static final String MESSAGE_SUCCESS = "Undid the last change.";
    public static final String MESSAGE_NOTHING = "Nothing to undo.";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Undoes the most recent change made to the address book.\n"
            + "Format: " + COMMAND_WORD + "\n"
            + "Example: " + COMMAND_WORD;


    @Override
    public CommandResult execute(Model model) {
        if (!model.canUndo()) {
            return new CommandResult(MESSAGE_NOTHING);
        }
        model.undo();
        return new CommandResult(MESSAGE_SUCCESS);
    }
    @Override
    public boolean equals(Object other) {
        return other == this || other instanceof UndoCommand;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
