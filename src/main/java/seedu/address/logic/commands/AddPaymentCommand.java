package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.payment.Amount;
import seedu.address.model.payment.Payment;
import seedu.address.model.person.Person;

/**
 * Adds a payment to one or more persons identified by their indexes in the displayed list.
 */
public class AddPaymentCommand extends Command {

    public static final String COMMAND_WORD = "addpayment";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds a payment to one or more persons identified by their index as displayed in the person list.\n"
            + "Parameters: INDEX[,INDEX]... a/AMOUNT d/DATE [r/REMARKS]\n"
            + "Example (single index): " + COMMAND_WORD + " 1 a/23.50 d/2025-10-09 r/taxi home\n"
            + "Example (multiple indexes): " + COMMAND_WORD + " 1,2,5 a/23.50 d/2025-10-09 r/taxi home";

    public static final String MESSAGE_SUCCESS_TEMPLATE = "Added payment %s to %s";

    private final List<Index> indexes;
    private final Amount amount;
    private final LocalDate date;
    private final String remarks;

    /**
     * Constructs an {@code AddPaymentCommand} to add a payment to one or more persons.
     */
    public AddPaymentCommand(List<Index> indexes, Amount amount, LocalDate date, String remarks) {
        requireNonNull(indexes);
        requireNonNull(amount);
        requireNonNull(date);

        this.indexes = List.copyOf(indexes);
        this.amount = amount;
        this.date = date;
        this.remarks = remarks;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        var list = model.getFilteredPersonList();

        Payment payment = new Payment(amount, date, remarks);
        List<String> updatedNames = new ArrayList<>();

        for (Index index : indexes) {
            if (index.getZeroBased() >= list.size()) {
                throw new CommandException(MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }

            Person target = list.get(index.getZeroBased());
            Person updated = target.withAddedPayment(payment);
            model.setPerson(target, updated);
            updatedNames.add(updated.getName().toString());
        }

        String joinedNames = String.join(", ", updatedNames);
        String message = String.format(MESSAGE_SUCCESS_TEMPLATE, payment, joinedNames);
        return new CommandResult(message);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof AddPaymentCommand
                && indexes.equals(((AddPaymentCommand) other).indexes)
                && amount.equals(((AddPaymentCommand) other).amount)
                && date.equals(((AddPaymentCommand) other).date)
                && ((AddPaymentCommand) other).remarks.equals(remarks));
    }
}
