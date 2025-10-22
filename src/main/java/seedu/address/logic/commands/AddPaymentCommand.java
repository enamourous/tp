package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.payment.Amount;
import seedu.address.model.payment.Payment;
import seedu.address.model.person.Person;

/**
 * Adds a payment to a person identified by the index number in the displayed list.
 */
public class AddPaymentCommand extends Command {
    public static final String COMMAND_WORD = "addpayment";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds a payment to one or more persons identified by their index as displayed in the person list.\n"
            + "Parameters: INDEX[,INDEX]... a/AMOUNT d/DATE [r/REMARKS]\n"
            + "Example (single index): " + COMMAND_WORD + " 1 a/23.50 d/2025-10-09 r/taxi home\n"
            + "Example (multiple indexes): " + COMMAND_WORD + " 1,2,5 a/23.50 d/2025-10-09 r/taxi home";

    private static final Logger logger = LogsCenter.getLogger(AddPaymentCommand.class);

    private final List<Index> indexes;
    private final Payment payment;

    /**
     * Constructs an {@code AddPaymentCommand} to add a payment to one or more persons.
     *
     * @param indexes The list of one-based indexes of persons in the displayed list to add the payment to.
     * @param amount The amount of the payment.
     * @param date The date of the payment.
     * @param remarks Optional remarks for the payment. Can be null.
     * @throws NullPointerException if {@code indexes}, {@code amount}, or {@code date} is null.
     */
    public AddPaymentCommand(List<Index> indexes, Amount amount, LocalDate date, String remarks) {
        requireNonNull(indexes);
        requireNonNull(amount);
        requireNonNull(date);

        this.indexes = List.copyOf(indexes); // defensive copy
        this.payment = new Payment(amount, date, remarks);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        logger.fine("Executing AddPaymentCommand with " + indexes.size() + " target(s).");

        var list = model.getFilteredPersonList();

        List<String> updatedNames = new ArrayList<>();

        for (Index index : indexes) {
            if (index.getZeroBased() >= list.size()) {
                logger.warning("Invalid person index: " + index.getOneBased());
                throw new CommandException(MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }

            Person target = list.get(index.getZeroBased());
            logger.fine("Adding payment to: " + target.getName());

            Person updated = target.withAddedPayment(payment);

            model.setPerson(target, updated);
            updatedNames.add(updated.getName().toString());
        }

        String joinedNames = String.join(", ", updatedNames);
        String message = String.format("Added payment %s to %s", payment, joinedNames);
        logger.info("Successfully added payment: " + payment + " to " + joinedNames);

        return new CommandResult(message);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof AddPaymentCommand
                && indexes.equals(((AddPaymentCommand) other).indexes)
                && payment.equals(((AddPaymentCommand) other).payment));
    }
}
