package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

public class UndoCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
    }

    @Test
    public void execute_nothingToUndo_showsMessageAndNoChange() {
        // No snapshots saved yet
        assertCommandSuccess(new UndoCommand(), model, UndoCommand.MESSAGE_NOTHING, expectedModel);
    }

    @Test
    public void execute_afterMutatingCommand_restoresPreviousState() {
        // Baseline expected state is the original (before mutation)
        Person original = model.getFilteredPersonList().get(0);

        // Simulate a mutating command the way LogicManager would:
        model.saveSnapshot(); // snapshot "before"
        model.setPerson(original, original.withArchived(true)); // mutate

        // After undo, model should match expectedModel (original state)
        assertCommandSuccess(new UndoCommand(), model, UndoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void equals() {
        UndoCommand a = new UndoCommand();
        UndoCommand b = new UndoCommand();

        assertTrue(a.equals(a)); // same object
        assertTrue(a.equals(b)); // stateless shld be equal
        assertTrue(!a.equals(null)); // null
        assertTrue(!a.equals(new RedoCommand())); // different type
    }
}
