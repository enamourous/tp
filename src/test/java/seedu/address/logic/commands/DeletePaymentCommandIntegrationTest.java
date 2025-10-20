package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.payment.Amount;
import seedu.address.model.payment.Payment;
import seedu.address.model.person.Person;

/**
 * Integration tests for {@code DeletePaymentCommand} interacting with the Model.
 */
public class DeletePaymentCommandIntegrationTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_deletePaymentSinglePerson_success() throws Exception {
        Person targetPerson = model.getFilteredPersonList().get(0);
        Payment payment = new Payment(new Amount(new BigDecimal("50.00")), LocalDate.of(2025, 1, 1));
        Person personWithPayment = targetPerson.withAddedPayment(payment);

        model.setPerson(targetPerson, personWithPayment);

        DeletePaymentCommand command = new DeletePaymentCommand(
                List.of(Index.fromOneBased(1)),
                Index.fromOneBased(1)
        );

        // Prepare expected model
        Person updatedPerson = personWithPayment.withRemovedPayment(payment);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personWithPayment, updatedPerson);

        String expectedMessage = String.format(DeletePaymentCommand.MESSAGE_SUCCESS, 1, updatedPerson.getName());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_deletePaymentMultiplePersons_success() throws Exception {
        Payment payment = new Payment(new Amount(new BigDecimal("20.00")), LocalDate.of(2025, 1, 1));

        // Add payment to first two persons
        Person person1 = model.getFilteredPersonList().get(0).withAddedPayment(payment);
        Person person2 = model.getFilteredPersonList().get(1).withAddedPayment(payment);

        model.setPerson(model.getFilteredPersonList().get(0), person1);
        model.setPerson(model.getFilteredPersonList().get(1), person2);

        DeletePaymentCommand command = new DeletePaymentCommand(
                List.of(Index.fromOneBased(1), Index.fromOneBased(2)),
                Index.fromOneBased(1)
        );

        // Prepare expected model
        Person updatedPerson1 = person1.withRemovedPayment(payment);
        Person updatedPerson2 = person2.withRemovedPayment(payment);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(person1, updatedPerson1);
        expectedModel.setPerson(person2, updatedPerson2);

        String expectedMessage = String.format(DeletePaymentCommand.MESSAGE_SUCCESS, 1,
                updatedPerson1.getName() + ", " + updatedPerson2.getName());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPaymentIndex_throwsCommandException() {
        Person targetPerson = model.getFilteredPersonList().get(0);
        DeletePaymentCommand command = new DeletePaymentCommand(
                List.of(Index.fromOneBased(1)),
                Index.fromOneBased(1) // person has no payments
        );

        assertCommandFailure(command, model,
                String.format(DeletePaymentCommand.MESSAGE_INVALID_PAYMENT_INDEX, targetPerson.getName()));
    }

    @Test
    public void execute_emptyModel_throwsCommandException() {
        Model emptyModel = new ModelManager();
        Index personIndex = Index.fromOneBased(1);
        Index paymentIndex = Index.fromOneBased(1);

        DeletePaymentCommand command = new DeletePaymentCommand(List.of(personIndex), paymentIndex);

        assertCommandFailure(command, emptyModel, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidPersonIndex_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        int outOfBoundsIndex = model.getFilteredPersonList().size() + 1;

        DeletePaymentCommand command = new DeletePaymentCommand(
                List.of(Index.fromOneBased(outOfBoundsIndex)),
                Index.fromOneBased(1)
        );

        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }
}
