package seedu.address.logic.commands;

import seedu.address.model.Model;

/**
 * Reapplies the most recently undone change in the address book (i.e., performs a redo operation).
 * <p>
 * The {@code RedoCommand} relies on the {@link Model} to maintain a redo stack containing
 * previously undone states. If there are no actions available to redo, a message is shown
 * to inform the user.
 */
public class RedoCommand extends Command {
    public static final String COMMAND_WORD = "redo";
    public static final String MESSAGE_SUCCESS = "Redid the last undone change.";
    public static final String MESSAGE_NOTHING = "Nothing to redo.";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Redoes the last undone change in the address book.\n"
            + "Format: " + COMMAND_WORD + "\n"
            + "Example: " + COMMAND_WORD;

    /**
     * Executes the redo operation.
     *
     * <p>If the {@link Model} has no redoable states (i.e., {@code model.canRedo()} is false),
     * this method returns a {@link CommandResult} with {@link #MESSAGE_NOTHING}.
     * Otherwise, it re-applies the most recently undone change using {@link Model#redo()}
     * and returns a {@link CommandResult} with {@link #MESSAGE_SUCCESS}.
     *
     * @param model The model in which to perform the redo operation.
     * @return The result of executing the redo command, containing feedback for the user.
     */
    @Override
    public CommandResult execute(Model model) {
        if (!model.canRedo()) {
            return new CommandResult(MESSAGE_NOTHING);
        }
        model.redo();
        return new CommandResult(MESSAGE_SUCCESS);
    }

    /**
     * Returns true if this command is equal to the given object.
     * <p>
     * Two {@code RedoCommand} instances are considered equal because this command is stateless
     * and identical in behavior across all instances.
     *
     * @param other The object to compare with.
     * @return {@code true} if the other object is also a {@code RedoCommand}, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object other) {
        return other == this || other instanceof RedoCommand;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
