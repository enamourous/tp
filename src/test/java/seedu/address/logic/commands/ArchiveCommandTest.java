package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
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

/** Tests for ArchiveCommand (supports multiple indices). */
public class ArchiveCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_singleIndex_success() {
        Person toArchive = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        ArchiveCommand command = new ArchiveCommand(List.of(INDEX_FIRST_PERSON));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        Person archived = toArchive.withArchived(true);
        expectedModel.setPerson(toArchive, archived);
        expectedModel.updateFilteredPersonList(Model.PREDICATE_SHOW_ACTIVE_PERSONS);

        // default list should show only active; the command itself sets active predicate
        String expectedMessage = String.format(ArchiveCommand.MESSAGE_SUCCESS, archived.getName());

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        // after success, archived person should not be in active filtered list
        assertTrue(model.getFilteredPersonList().stream().noneMatch(p -> p.isArchived()));
    }

    @Test
    public void execute_multipleIndices_success() {
        Person p1 = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person p2 = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());

        ArchiveCommand command = new ArchiveCommand(Arrays.asList(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        Person a1 = p1.withArchived(true);
        Person a2 = p2.withArchived(true);
        expectedModel.setPerson(p1, a1);
        expectedModel.setPerson(p2, a2);
        expectedModel.updateFilteredPersonList(Model.PREDICATE_SHOW_ACTIVE_PERSONS);

        String expectedMessage = String.format(
                ArchiveCommand.MESSAGE_SUCCESS,
                a1.getName() + ", " + a2.getName()
        );

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_indexOutOfBounds_throwsCommandException() {
        int outOfBounds = model.getFilteredPersonList().size() + 1;
        ArchiveCommand command = new ArchiveCommand(List.of(Index.fromOneBased(outOfBounds)));

        assertCommandFailure(command, model,
                seedu.address.commons.core.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_alreadyArchived_throwsCommandException() {
        // archive first
        Person p = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        model.setPerson(p, p.withArchived(true));

        ArchiveCommand command = new ArchiveCommand(List.of(INDEX_FIRST_PERSON));

        // Expect MESSAGE_ALREADY_ARCHIVED to include the person's name
        assertCommandFailure(command, model,
                String.format(ArchiveCommand.MESSAGE_ALREADY_ARCHIVED, p.getName()));
    }

    @Test
    public void equals() {
        ArchiveCommand c1 = new ArchiveCommand(List.of(INDEX_FIRST_PERSON));
        ArchiveCommand c2 = new ArchiveCommand(List.of(INDEX_FIRST_PERSON));
        ArchiveCommand c3 = new ArchiveCommand(Arrays.asList(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON));

        // same values returns true
        assertTrue(c1.equals(c2));
        // different targets -> false
        assertTrue(!c1.equals(c3));
        // same object returns true
        assertTrue(c1.equals(c1));
        // null / different type returns false
        assertTrue(!c1.equals(null));
        assertTrue(!c1.equals(new ListCommand()));
    }
}

