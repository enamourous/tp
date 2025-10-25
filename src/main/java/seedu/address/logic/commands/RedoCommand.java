package seedu.address.logic.commands;

import seedu.address.model.Model;

/**
 * Performs a redo on the most recent undo action
 */
public class RedoCommand extends Command {
    public static final String COMMAND_WORD = "redo";
    public static final String MESSAGE_SUCCESS = "Redid the last undone change.";
    public static final String MESSAGE_NOTHING = "Nothing to redo.";

    @Override
    public CommandResult execute(Model model) {
        if (!model.canRedo()) {
            return new CommandResult(MESSAGE_NOTHING);
        }
        model.redo();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
