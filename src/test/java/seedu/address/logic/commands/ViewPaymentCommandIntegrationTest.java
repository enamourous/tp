package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.testutil.TypicalPersons;

/**
 * Integration tests for {@link ViewPaymentCommand}.
 */
public class ViewPaymentCommandIntegrationTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_validIndex_success() throws Exception {
        CommandResult result = new ViewPaymentCommand(Index.fromOneBased(1)).execute(model);
        assertNotNull(result);
        assertTrue(result.getFeedbackToUser() != null && !result.getFeedbackToUser().isEmpty());
    }

    @Test
    public void execute_outOfBoundsIndex_throwsCommandException() {
        int outOfBounds = model.getFilteredPersonList().size() + 1;
        ViewPaymentCommand cmd = new ViewPaymentCommand(Index.fromOneBased(outOfBounds));
        assertThrows(CommandException.class, () -> cmd.execute(model));
    }
}
