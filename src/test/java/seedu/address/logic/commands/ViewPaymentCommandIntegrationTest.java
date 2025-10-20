package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PaymentBuilder;
import seedu.address.testutil.PersonBuilder;

public class ViewPaymentCommandIntegrationTest {

    @Test
    public void execute_success_twoPayments_includesCountOrLines() throws Exception {
        var p1 = new PaymentBuilder().withAmount("23.00").withDate("2024-10-10").withRemarks("CCA Shirt").build();
        var p2 = new PaymentBuilder().withAmount("12.50").withDate("2024-11-01").withRemarks("CCA Badge").build();
        Person alice = new PersonBuilder().withName("Alice").withPayments(p1, p2).build();

        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        model.addPerson(alice);

        CommandResult res = new ViewPaymentCommand(Index.fromOneBased(1)).execute(model);
        String msg = res.getFeedbackToUser().toLowerCase();

        assertTrue(msg.contains("payment"));
        assertTrue(msg.contains("2"));
    }

    @Test
    public void execute_success_noPayments_showsNoneOrZero() throws Exception {
        Person bob = new PersonBuilder().withName("Bob").withPayments().build();
        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        model.addPerson(bob);

        CommandResult res = new ViewPaymentCommand(Index.fromOneBased(1)).execute(model);
        String msg = res.getFeedbackToUser().toLowerCase();

        assertTrue(msg.contains("no payments") || msg.contains("0"));
    }

    @Test
    public void execute_invalidPersonIndex_throws() {
        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        assertThrows(CommandException.class, () ->
                new ViewPaymentCommand(Index.fromOneBased(1)).execute(model));
    }
}
