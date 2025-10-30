package seedu.address.logic.commands;

import seedu.address.model.Model;

/**
 * Reverts the most recent mutating action performed in the address book (i.e., performs an undo operation).
 * <p>
 * The {@code UndoCommand} relies on the {@link Model} to maintain an internal history of previous states.
 * If there are no actions left to undo, a message is shown to inform the user.
 */
public class UndoCommand extends Command {
    public static final String COMMAND_WORD = "undo";
    public static final String MESSAGE_SUCCESS = "Undid the last change.";
    public static final String MESSAGE_NOTHING = "Nothing to undo.";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Undoes the most recent change made to the address book.\n"
            + "Format: " + COMMAND_WORD + "\n"
            + "Example: " + COMMAND_WORD;

    /**
     * Executes the undo operation.
     *
     * <p>If the {@link Model} has no previous states to revert to (i.e., {@code model.canUndo()} is false),
     * this method returns a {@link CommandResult} with {@link #MESSAGE_NOTHING}.
     * Otherwise, it performs the undo operation using {@link Model#undo()} and
     * returns a {@link CommandResult} with {@link #MESSAGE_SUCCESS}.
     *
     * @param model The model in which to perform the undo operation.
     * @return The result of executing the undo command, containing feedback for the user.
     */
    @Override
    public CommandResult execute(Model model) {
        if (!model.canUndo()) {
            return new CommandResult(MESSAGE_NOTHING);
        }
        model.undo();
        return new CommandResult(MESSAGE_SUCCESS);
    }

    /**
     * Returns true if this command is equal to the given object.
     * <p>
     * Two {@code UndoCommand} instances are considered equal as they are stateless and identical in behavior.
     *
     * @param other The object to compare with.
     * @return {@code true} if the other object is also an {@code UndoCommand}, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object other) {
        return other == this || other instanceof UndoCommand;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
