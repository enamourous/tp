package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.payment.Payment;
import seedu.address.model.person.Person;

/**
 * Deletes one or more payments (by their display indices) from a single person.
 */
public class DeletePaymentCommand extends Command {

    public static final String COMMAND_WORD = "deletepayment";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes one or more payments from the person identified by the displayed index.\n"
            + "Parameters: PERSON_INDEX p/PAYMENT_INDEX[,PAYMENT_INDEX]...\n"
            + "Notes: PAYMENT_INDEX refers to the display order shown by 'viewpayment'. Duplicates are ignored.\n"
            + "Example: " + COMMAND_WORD + " 2 p/1,2,3";

    public static final String MESSAGE_SUCCESS = "Deleted payment(s) #%s from %s";
    public static final String MESSAGE_INVALID_PAYMENT_INDEX = "Invalid payment index #%d for person: %s";

    private final Index personIndex; // one person (1-based input)
    private final List<Index> paymentIndexes; // one or more payment indices (1-based input)

    /**
     * Creates a DeletePaymentCommand to delete the specified payment(s).
     */
    public DeletePaymentCommand(Index personIndex, List<Index> paymentIndexes) {
        requireNonNull(personIndex);
        requireNonNull(paymentIndexes);
        this.personIndex = personIndex;
        this.paymentIndexes = List.copyOf(paymentIndexes);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        final List<Person> lastShownList = model.getFilteredPersonList();
        final int pZero = personIndex.getZeroBased();

        if (pZero < 0 || pZero >= lastShownList.size()) {
            throw new CommandException(MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        final Person target = lastShownList.get(pZero);
        final List<Payment> displayList = Payment.inDisplayOrder(target.getPayments());

        if (displayList.isEmpty()) {
            throw new CommandException("This person has no payments to delete.");
        }

        final List<Integer> zeroBased = paymentIndexes.stream()
                .map(Index::getZeroBased)
                .toList();

        // Validate all indices first
        for (int z : zeroBased) {
            if (z >= displayList.size()) {
                throw new CommandException(String.format(
                        MESSAGE_INVALID_PAYMENT_INDEX, z + 1, target.getName()));
            }
        }

        // Resolve payments to delete
        final List<Payment> toDelete = zeroBased.stream()
                .map(displayList::get)
                .toList();

        // Apply deletion (by identity, order doesn't matter)
        Person updated = target;
        for (Payment pay : toDelete) {
            updated = updated.withRemovedPayment(pay);
        }
        model.setPerson(target, updated);

        // Build success message
        final String joined = paymentIndexes.stream()
                .map(i -> Integer.toString(i.getOneBased()))
                .collect(java.util.stream.Collectors.joining(","));

        return new CommandResult(String.format(MESSAGE_SUCCESS, joined, updated.getName()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof DeletePaymentCommand)
                && personIndex.equals(((DeletePaymentCommand) other).personIndex)
                && paymentIndexes.equals(((DeletePaymentCommand) other).paymentIndexes);
    }

    @Override
    public boolean isMutating() {
        return true;
    }
}
