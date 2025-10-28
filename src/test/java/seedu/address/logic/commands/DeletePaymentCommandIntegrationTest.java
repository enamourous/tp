package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.payment.Amount;
import seedu.address.model.payment.Payment;
import seedu.address.model.person.Person;

public class DeletePaymentCommandIntegrationTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_deleteSinglePayment_success() throws Exception {
        Person targetPerson = model.getFilteredPersonList().get(0);
        Payment payment = new Payment(new Amount(new BigDecimal("50.00")), LocalDate.of(2025, 1, 1));
        Person withPayment = targetPerson.withAddedPayment(payment);
        model.setPerson(targetPerson, withPayment);

        DeletePaymentCommand command = new DeletePaymentCommand(
                Index.fromOneBased(1), List.of(Index.fromOneBased(1)));

        Person updated = withPayment.withRemovedPayment(payment);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(withPayment, updated);

        String expectedMessage = String.format(DeletePaymentCommand.MESSAGE_SUCCESS, "1", updated.getName());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPaymentIndex_throwsCommandException() {
        Person target = model.getFilteredPersonList().get(0);
        DeletePaymentCommand command = new DeletePaymentCommand(
                Index.fromOneBased(1), List.of(Index.fromOneBased(1)));

        assertCommandFailure(command, model,
                "This person has no payments to delete.");
    }

    @Test
    public void execute_invalidPersonIndex_throwsCommandException() {
        int outOfBounds = model.getFilteredPersonList().size() + 1;
        DeletePaymentCommand command = new DeletePaymentCommand(
                Index.fromOneBased(outOfBounds), List.of(Index.fromOneBased(1)));

        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }
}
