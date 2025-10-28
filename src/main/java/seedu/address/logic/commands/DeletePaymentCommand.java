package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.payment.Payment;
import seedu.address.model.person.Person;

/**
 * Deletes a payment from one or more persons identified by their indexes in the displayed person list.
 * The payment index refers to the index shown by 'viewpayment', which uses display order.
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
    private final Index paymentIndex; // index within the person's DISPLAY list

    /**
     * Creates a {@code DeletePaymentCommand}.
     *
     * @param personIndexes list of person indexes in the currently displayed list (1-based input).
     * @param paymentIndex  the payment index within each person's display-ordered payment list (1-based input).
     * @throws NullPointerException if any parameter is {@code null}.
     */
    public DeletePaymentCommand(List<Index> personIndexes, Index paymentIndex) {
        requireNonNull(personIndexes);
        requireNonNull(paymentIndex);
        this.personIndexes = List.copyOf(personIndexes);
        this.paymentIndex = paymentIndex;
    }

    /**
     * Deletes the specified payment (resolved using the same display order as {@code viewpayment})
     * from each targeted person, then updates the model.
     *
     * @param model model holding the current filtered person list.
     * @return a {@link CommandResult} summarising the deletions.
     * @throws CommandException if any person index is invalid, or the payment index does not exist for a person.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (lastShownList.isEmpty()) {
            throw new CommandException(MESSAGE_INVALID_PERSON_INDEX_FOR_DELETE_PAYMENT);
        }

        // First pass: validate and resolve the exact Payment objects to delete in display order
        // Use LinkedHashMap to keep output names in the same order as the input personIndexes
        Map<Person, Payment> deletions = new LinkedHashMap<>();

        for (Index personIndex : personIndexes) {
            int zero = personIndex.getZeroBased();
            if (zero >= lastShownList.size()) {
                throw new CommandException(MESSAGE_INVALID_PERSON_INDEX_FOR_DELETE_PAYMENT);
            }

            Person target = lastShownList.get(zero);

            // Build the SAME display list as viewpayment
            List<Payment> displayList = Payment.inDisplayOrder(target.getPayments());

            int payZero = paymentIndex.getZeroBased();
            if (payZero >= displayList.size()) {
                throw new CommandException(String.format(MESSAGE_INVALID_PAYMENT_INDEX, target.getName()));
            }

            Payment toDelete = displayList.get(payZero);
            deletions.put(target, toDelete);
        }

        // Second pass: apply all updates
        List<String> updatedNames = new ArrayList<>();
        for (Map.Entry<Person, Payment> entry : deletions.entrySet()) {
            Person target = entry.getKey();
            Payment toDelete = entry.getValue();

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

    @Override
    public boolean isMutating() {
        return true;
    }
}
