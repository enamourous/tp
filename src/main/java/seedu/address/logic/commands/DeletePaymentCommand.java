package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.payment.Payment;
import seedu.address.model.person.Person;

/**
 * Deletes a payment from a person identified by the index number in the displayed list.
 */
public class DeletePaymentCommand extends Command {

    public static final String COMMAND_WORD = "deletepayment";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes a payment from one or more persons identified by their indexes, "
            + "as shown in the displayed person list.\n"
            + "Parameters: PERSON_INDEX[,PERSON_INDEX]... p/PAYMENT_INDEX\n"
            + "Example: " + COMMAND_WORD + " 1 p/2";

    public static final String MESSAGE_SUCCESS = "Deleted payment #%d from %s";
    public static final String MESSAGE_INVALID_PAYMENT_INDEX = "Invalid payment index for person: %s";
    public static final String MESSAGE_INVALID_PERSON_INDEX_FOR_DELETE_PAYMENT = "The person index provided is invalid";

    private final List<Index> personIndexes;
    private final Index paymentIndex; // index within the person's payment list

    /**
     * Constructs a {@code DeletePaymentCommand} to delete a payment from one or more persons.
     *
     * @param personIndexes The indexes of the persons in the displayed list.
     * @param paymentIndex The index of the payment to delete within each person’s payment list.
     */
    public DeletePaymentCommand(List<Index> personIndexes, Index paymentIndex) {
        requireNonNull(personIndexes);
        requireNonNull(paymentIndex);
        this.personIndexes = List.copyOf(personIndexes);
        this.paymentIndex = paymentIndex;
    }

    /**
     * Executes the command and deletes the specified payment(s) from the target person(s).
     *
     * @param model {@code Model} which the command should operate on.
     * @return A {@code CommandResult} with the success message.
     * @throws CommandException If the person index is invalid, or the payment index is invalid.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();
        List<String> updatedNames = new ArrayList<>();

        // Check if model is empty
        if (lastShownList.isEmpty()) {
            throw new CommandException(MESSAGE_INVALID_PERSON_INDEX_FOR_DELETE_PAYMENT);
        }

        for (Index personIndex : personIndexes) {
            if (personIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(MESSAGE_INVALID_PERSON_INDEX_FOR_DELETE_PAYMENT);
            }

            Person target = lastShownList.get(personIndex.getZeroBased());
            List<Payment> payments = target.getPayments();

            if (paymentIndex.getZeroBased() >= payments.size()) {
                throw new CommandException(String.format(MESSAGE_INVALID_PAYMENT_INDEX, target.getName()));
            }

            Payment toDelete = payments.get(paymentIndex.getZeroBased());
            Person updated = target.withRemovedPayment(toDelete);

            model.setPerson(target, updated);
            updatedNames.add(updated.getName().toString());
        }

        String joinedNames = String.join(", ", updatedNames);
        String message = String.format(MESSAGE_SUCCESS, paymentIndex.getOneBased(), joinedNames);
        return new CommandResult(message);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof DeletePaymentCommand
                && personIndexes.equals(((DeletePaymentCommand) other).personIndexes)
                && paymentIndex.equals(((DeletePaymentCommand) other).paymentIndex));
    }
}
