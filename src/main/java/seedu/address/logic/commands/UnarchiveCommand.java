package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ACTIVE_PERSONS;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Unarchives one or more persons identified by their indexes in the displayed list.
 */
public class UnarchiveCommand extends Command {

    public static final String COMMAND_WORD = "unarchive";

    public static final String MESSAGE_USAGE = COMMAND_WORD
        + ": Unarchives one or more persons identified by their indexes in the displayed person list.\n"
        + "Parameters: INDEX[,INDEX]... (each must be a positive integer)\n"
        + "Example (single): " + COMMAND_WORD + " 1\n"
        + "Example (multiple): " + COMMAND_WORD + " 1,2,5";

    public static final String MESSAGE_NOT_ARCHIVED = "One or more selected persons are not archived: %s";
    public static final String MESSAGE_SUCCESS = "Unarchived: %s";

    private final List<Index> targetIndexes;

    /**
     * Creates an {@code UnarchiveCommand} to unarchive the specified persons.
     *
     * @param targetIndexes the list of person indexes to unarchive.
     */
    public UnarchiveCommand(List<Index> targetIndexes) {
        requireNonNull(targetIndexes);
        // remove duplicates while preserving user input order
        Set<Integer> seenIndexes = new LinkedHashSet<>();
        this.targetIndexes = targetIndexes.stream()
            .filter(index -> seenIndexes.add(index.getZeroBased()))
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> displayedPersons = model.getFilteredPersonList();

        // pre validation
        List<Person> personsToUnarchive = new ArrayList<>(targetIndexes.size());
        List<String> notArchivedNames = new ArrayList<>();

        for (Index targetIndex : targetIndexes) {
            int zeroBasedIndex = targetIndex.getZeroBased();
            if (zeroBasedIndex >= displayedPersons.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }

            Person person = displayedPersons.get(zeroBasedIndex);
            if (!person.isArchived()) {
                notArchivedNames.add(person.getName().toString());
            }
            personsToUnarchive.add(person);
        }

        if (!notArchivedNames.isEmpty()) {
            throw new CommandException(String.format(MESSAGE_NOT_ARCHIVED,
                String.join(", ", notArchivedNames)));
        }

        // apply updates
        List<String> unarchivedNames = new ArrayList<>(personsToUnarchive.size());
        for (Person originalPerson : personsToUnarchive) {
            Person unarchivedPerson = originalPerson.withArchived(false);
            model.setPerson(originalPerson, unarchivedPerson);
            unarchivedNames.add(unarchivedPerson.getName().toString());
        }

        model.updateFilteredPersonList(PREDICATE_SHOW_ACTIVE_PERSONS);

        return new CommandResult(String.format(MESSAGE_SUCCESS, String.join(", ", unarchivedNames)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof UnarchiveCommand)) {
            return false;
        }
        UnarchiveCommand otherCommand = (UnarchiveCommand) other;
        return targetIndexes.equals(otherCommand.targetIndexes);
    }

    @Override
    public boolean isMutating() {
        return true;
    }
}
