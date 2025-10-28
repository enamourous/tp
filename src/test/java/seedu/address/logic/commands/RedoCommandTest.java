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

public class RedoCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
    }

    @Test
    public void execute_nothingToRedo_showsMessageAndNoChange() {
        // No undo performed yet, so redo has nothing to apply
        assertCommandSuccess(new RedoCommand(), model, RedoCommand.MESSAGE_NOTHING, expectedModel);
    }

    @Test
    public void execute_afterUndo_restoresUndoneChange() {
        Person p0 = model.getFilteredPersonList().get(0);

        // Simulate a mutating command
        model.saveSnapshot();
        model.setPerson(p0, p0.withArchived(true));

        // Undo, now redo should be available
        model.undo();

        // Expected model should represent the mutated state (archived=true) AFTER redo
        Person exp0 = expectedModel.getFilteredPersonList().get(0);
        expectedModel.setPerson(exp0, exp0.withArchived(true));
        expectedModel.updateFilteredPersonList(seedu.address.model.Model.PREDICATE_SHOW_ACTIVE_PERSONS);

        // Now redo should re-apply the archived change and match expectedModel
        assertCommandSuccess(new RedoCommand(), model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_afterSingleRedo_secondRedoDoesNothing() {
        Person p0 = model.getFilteredPersonList().get(0);

        // Mutate with a snapshot, then undo, so one redo available
        model.saveSnapshot();
        model.setPerson(p0, p0.withArchived(true));
        model.undo();

        // Expected state after the FIRST redo: archived = true and active member list
        Person exp0 = expectedModel.getFilteredPersonList().get(0);
        expectedModel.setPerson(exp0, exp0.withArchived(true));
        expectedModel.updateFilteredPersonList(seedu.address.model.Model.PREDICATE_SHOW_ACTIVE_PERSONS);

        // First redo succeeds
        assertCommandSuccess(new RedoCommand(), model, RedoCommand.MESSAGE_SUCCESS, expectedModel);

        // Second redo has nothing left to apply; model stays identical to expectedModel
        assertCommandSuccess(new RedoCommand(), model, RedoCommand.MESSAGE_NOTHING, expectedModel);
    }

    @Test
    public void equals() {
        RedoCommand a = new RedoCommand();
        RedoCommand b = new RedoCommand();

        assertTrue(a.equals(a)); // same object
        assertTrue(a.equals(b)); // stateless shld be equal
        assertTrue(!a.equals(null)); // null
        assertTrue(!a.equals(new UndoCommand())); // different type
    }
}
