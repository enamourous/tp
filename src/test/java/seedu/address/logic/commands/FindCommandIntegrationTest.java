package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;

/**
 * Integration tests for {@code FindCommand} interacting with payments in the model.
 *
 * Test scheme
 *  1. Persons with payments remain discoverable via FindCommand.
 *  2. Adding or deleting payments does not affect the find filtering logic.
 *  3. The filtered list reflects the expected persons after both AddPayment and Find commands.
 *  4. Find person - add payment - view payment workflow
 */
public class FindCommandIntegrationTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    // 1. Persons with payments remain discoverable via find command
    @Test
    public void execute_findAfterAddingPayments_success() throws Exception {
        // get a person from the model and add a payment
        Person target = model.getFilteredPersonList().get(0);
        Payment payment = new Payment(new Amount(new BigDecimal("50.00")),
                LocalDate.of(2025, 10, 20), "grabfood");
        Person updated = target.withAddedPayment(payment);

        model.setPerson(target, updated);
        expectedModel.setPerson(target, updated);

        // run find command by name keyword that matches this person
        String keyword = updated.getName().fullName.split(" ")[0];
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(List.of(keyword));
        FindCommand command = new FindCommand(predicate);

        expectedModel.updateFilteredPersonList(
                Model.PREDICATE_SHOW_ACTIVE_PERSONS.and(predicate));

        CommandResult result = command.execute(model);

        assertEquals(String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW,
                expectedModel.getFilteredPersonList().size()), result.getFeedbackToUser());
        assertEquals(expectedModel.getFilteredPersonList(), model.getFilteredPersonList());
    }

    // 2. Adding or deleting payments does not affect the find filtering logic.
    @Test
    public void execute_findAfterDeletingPayments_success() throws Exception {
        // add and then remove a payment, find still works
        Person target = model.getFilteredPersonList().get(0);
        Payment payment = new Payment(new Amount(new BigDecimal("30.00")),
                LocalDate.of(2025, 10, 10), "shirt");
        Person withPayment = target.withAddedPayment(payment);
        model.setPerson(target, withPayment);

        // delete the payment
        Person noPayment = withPayment.withRemovedPayment(payment);
        model.setPerson(withPayment, noPayment);
        expectedModel.setPerson(target, noPayment);

        // find command should still locate the person
        String keyword = target.getName().fullName.split(" ")[0];
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(List.of(keyword));
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(Model.PREDICATE_SHOW_ACTIVE_PERSONS.and(predicate));

        CommandResult result = command.execute(model);

        assertEquals(String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW,
                expectedModel.getFilteredPersonList().size()), result.getFeedbackToUser());
        assertEquals(expectedModel.getFilteredPersonList(), model.getFilteredPersonList());
    }

    // 3. The filtered list reflects the expected persons after both AddPayment and Find commands.
    @Test
    public void execute_findAfterMultiplePersonsHavePayments_success() throws Exception {
        // add payments to multiple people, ensure find still filters correctly
        Person p1 = model.getFilteredPersonList().get(0);
        Person p2 = model.getFilteredPersonList().get(1);

        Payment payment = new Payment(new Amount(new BigDecimal("99.99")),
                LocalDate.of(2025, 5, 5), "booking fee");
        model.setPerson(p1, p1.withAddedPayment(payment));
        model.setPerson(p2, p2.withAddedPayment(payment));

        expectedModel.setPerson(p1, p1.withAddedPayment(payment));
        expectedModel.setPerson(p2, p2.withAddedPayment(payment));

        // find persons with a shared keyword
        String keyword = p1.getName().fullName.split(" ")[0];
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(List.of(keyword));
        FindCommand command = new FindCommand(predicate);

        expectedModel.updateFilteredPersonList(Model.PREDICATE_SHOW_ACTIVE_PERSONS.and(predicate));

        CommandResult result = command.execute(model);
        assertEquals(String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW,
                expectedModel.getFilteredPersonList().size()), result.getFeedbackToUser());
        assertEquals(expectedModel.getFilteredPersonList(), model.getFilteredPersonList());
    }

    // 4. Find person - add payment - view payment workflow
    @Test
    public void execute_findAddViewPaymentsFlow_success() throws Exception {
        // Step 1: find person by keyword
        Person target = model.getFilteredPersonList().get(0);
        String keyword = target.getName().fullName.split(" ")[0]; // first name
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(List.of(keyword));

        FindCommand findCommand = new FindCommand(predicate);
        findCommand.execute(model); // filters list
        assertEquals(1, model.getFilteredPersonList().size());
        assertEquals(target, model.getFilteredPersonList().get(0));

        // Step 2: add payment to that person
        Amount amount = new Amount(new BigDecimal("25.00"));
        LocalDate date = LocalDate.of(2025, 10, 21);
        String remarks = "membership fee";

        AddPaymentCommand addPaymentCommand = new AddPaymentCommand(
                List.of(Index.fromOneBased(1)), amount, date, remarks);

        CommandResult addResult = addPaymentCommand.execute(model);
        Person updated = model.getFilteredPersonList().get(0);

        Payment expectedPayment = new Payment(amount, date, remarks);
        String expectedAddMessage = String.format(AddPaymentCommand.MESSAGE_SUCCESS_TEMPLATE,
                expectedPayment, target.getName());
        assertEquals(expectedAddMessage, addResult.getFeedbackToUser());
        assertEquals(1, updated.getPayments().size());

        // Step 3: view payments for that person
        ViewPaymentCommand viewCommand = ViewPaymentCommand.forIndex(Index.fromOneBased(1));
        CommandResult viewResult = viewCommand.execute(model);

        String expectedHeader = String.format("Payments for %s (1). Total: 25.00", updated.getName());
        String expectedBody = String.format("%s | %s | %s",
                date, amount, remarks);

        // Verify view output includes all key elements
        String actualMessage = viewResult.getFeedbackToUser();
        assertEquals(true, actualMessage.contains(expectedHeader));
        assertEquals(true, actualMessage.contains(expectedBody));
    }
}
