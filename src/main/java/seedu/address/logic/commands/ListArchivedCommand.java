package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ARCHIVED_PERSONS;

import seedu.address.model.Model;

/** Lists all archived persons (soft-deleted). */
public class ListArchivedCommand extends Command {

    public static final String COMMAND_WORD = "listarchived";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lists all archived persons.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_EMPTY = "No archived persons found. Use 'list' command to show active list.";
    public static final String MESSAGE_SUCCESS = "Listed all archived persons";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(PREDICATE_SHOW_ARCHIVED_PERSONS);
        if (model.getFilteredPersonList().isEmpty()) {
            return new CommandResult(MESSAGE_EMPTY);
        }
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        // Stateless command
        return other == this || (other instanceof ListArchivedCommand);
    }
}

