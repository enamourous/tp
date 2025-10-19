package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.model.Model.PREDICATE_SHOW_ARCHIVED_PERSONS;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

public class ListArchivedCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        // Ensure there is at least one archived person (same in both models)
        Person p = model.getFilteredPersonList().get(0);
        Person archived = p.withArchived(true);
        model.setPerson(p, archived);
        expectedModel.setPerson(p, archived);

        // Prepare expected model's filtered view to match the command's effect
        expectedModel.updateFilteredPersonList(PREDICATE_SHOW_ARCHIVED_PERSONS);
    }

    @Test
    public void execute_showsOnlyArchived() {
        assertCommandSuccess(
                new ListArchivedCommand(),
                model,
                ListArchivedCommand.MESSAGE_SUCCESS,
                expectedModel
        );

        // everything shown is archived
        assertTrue(model.getFilteredPersonList().stream().allMatch(Person::isArchived));
    }

    @Test
    public void equals() {
        ListArchivedCommand command1 = new ListArchivedCommand();
        ListArchivedCommand command2 = new ListArchivedCommand();

        // same object returns true
        assertTrue(command1.equals(command1));

        // same type and state return true
        assertTrue(command1.equals(command2));

        // null return false
        assertTrue(!command1.equals(null));

        // different type return false
        assertTrue(!command1.equals(new ListCommand()));
    }
}
