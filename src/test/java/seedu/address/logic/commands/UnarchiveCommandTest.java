package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.model.Model.PREDICATE_SHOW_ACTIVE_PERSONS;
import static seedu.address.model.Model.PREDICATE_SHOW_ARCHIVED_PERSONS;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

/** Tests for UnarchiveCommand (supports multiple indices). */
public class UnarchiveCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        // Archive two people in BOTH models so they stay in sync.
        Person p1 = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person p2 = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        model.setPerson(p1, p1.withArchived(true));
        model.setPerson(p2, p2.withArchived(true));
        expectedModel.setPerson(p1, p1.withArchived(true));
        expectedModel.setPerson(p2, p2.withArchived(true));

        // Mimic listarchived so only archived people are listed
        model.updateFilteredPersonList(PREDICATE_SHOW_ARCHIVED_PERSONS);
        expectedModel.updateFilteredPersonList(PREDICATE_SHOW_ARCHIVED_PERSONS);
    }

    @Test
    public void execute_singleIndex_success() {
        Person archived = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        UnarchiveCommand command = new UnarchiveCommand(List.of(INDEX_FIRST_PERSON));

        Person unarchived = archived.withArchived(false);
        expectedModel.setPerson(archived, unarchived);
        expectedModel.updateFilteredPersonList(PREDICATE_SHOW_ACTIVE_PERSONS);

        String expectedMessage = String.format(UnarchiveCommand.MESSAGE_SUCCESS, unarchived.getName());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_multipleIndices_success() {
        Person a1 = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person a2 = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());

        UnarchiveCommand command =
                new UnarchiveCommand(Arrays.asList(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON));

        Person u1 = a1.withArchived(false);
        Person u2 = a2.withArchived(false);
        expectedModel.setPerson(a1, u1);
        expectedModel.setPerson(a2, u2);

        expectedModel.updateFilteredPersonList(PREDICATE_SHOW_ACTIVE_PERSONS);

        String expectedMessage = String.format(
                UnarchiveCommand.MESSAGE_SUCCESS, u1.getName() + ", " + u2.getName());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_indexOutOfBounds_throwsCommandException() {
        int outOfBounds = model.getFilteredPersonList().size() + 1;
        UnarchiveCommand command = new UnarchiveCommand(List.of(Index.fromOneBased(outOfBounds)));

        assertCommandFailure(command, model, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Covers:
     *  - fetching each selected person
     *  - the (!person.isArchived()) branch, collecting names
     *  - throwing when notArchivedNames is non-empty
     */
    @Test
    public void execute_selectionContainsNotArchived_throwsWithNames() {
        Model localModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        // Archive the second person only; first remains active
        Person first = localModel.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person second = localModel.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        localModel.setPerson(second, second.withArchived(true));

        // Show ALL persons so both indices are selectable/visible
        localModel.updateFilteredPersonList(p -> true);

        UnarchiveCommand cmd =
                new UnarchiveCommand(Arrays.asList(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON));

        String expectedMessage = String.format(
                UnarchiveCommand.MESSAGE_NOT_ARCHIVED, first.getName().toString());

        assertCommandFailure(cmd, localModel, expectedMessage);
    }

    @Test
    public void execute_selectionAllNotArchived_throwsWithJoinedNames() {
        Model localModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        Person first = localModel.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person second = localModel.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        // Typical persons are active by default — no changes needed

        localModel.updateFilteredPersonList(p -> true);

        UnarchiveCommand cmd =
                new UnarchiveCommand(Arrays.asList(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON));

        String expectedMessage = String.format(
                UnarchiveCommand.MESSAGE_NOT_ARCHIVED, first.getName() + ", " + second.getName());

        assertCommandFailure(cmd, localModel, expectedMessage);
    }

    @Test
    public void equals() {
        UnarchiveCommand c1 = new UnarchiveCommand(List.of(INDEX_FIRST_PERSON));
        UnarchiveCommand c2 = new UnarchiveCommand(List.of(INDEX_FIRST_PERSON));
        UnarchiveCommand c3 = new UnarchiveCommand(Arrays.asList(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON));

        assertTrue(c1.equals(c2));
        assertTrue(!c1.equals(c3));
        assertTrue(c1.equals(c1));
        assertTrue(!c1.equals(null));
        assertTrue(!c1.equals(new ListCommand())); // different type
    }
}

