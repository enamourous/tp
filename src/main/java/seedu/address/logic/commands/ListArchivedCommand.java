package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ARCHIVED_PERSONS;

import seedu.address.model.Model;

/**
 * Lists all archived persons in the address book.
 * <p>
 * An archived person refers to one that has been soft-deleted,
 * meaning their data is still retained in storage but hidden from the active list.
 * This command allows users to view those archived entries.
 */

public class ListArchivedCommand extends Command {

    public static final String COMMAND_WORD = "listarchived";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lists all archived persons.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_EMPTY = "No archived persons found. Use 'list' command to show active list.";
    public static final String MESSAGE_SUCCESS = "Listed all archived persons";

    /**
     * Executes the command to display all archived persons.
     * <p>
     * The model’s filtered person list is updated to show only those
     * persons that satisfy {@code PREDICATE_SHOW_ARCHIVED_PERSONS}.
     *
     * @param model The {@code Model} which the command operates on.
     * @return A {@code CommandResult} containing the outcome message.
     * @throws NullPointerException If {@code model} is {@code null}.
     */
    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(PREDICATE_SHOW_ARCHIVED_PERSONS);
        if (model.getFilteredPersonList().isEmpty()) {
            return new CommandResult(MESSAGE_EMPTY);
        }
        return new CommandResult(MESSAGE_SUCCESS);
    }

    /**
     * Compares this command to another object for equality.
     * <p>
     * Since {@code ListArchivedCommand} is stateless, all instances are considered equal.
     *
     * @param other The other object to compare with.
     * @return {@code true} if the other object is also a {@code ListArchivedCommand}, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object other) {
        // Stateless command
        return other == this || (other instanceof ListArchivedCommand);
    }
}

