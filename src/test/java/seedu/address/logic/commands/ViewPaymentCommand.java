package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.payment.Payment;
import seedu.address.model.person.Person;

/**
 * Shows all payments of a person.
 */
public class ViewPaymentCommand extends Command {

    public static final String COMMAND_WORD = "viewpayment";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows all payments of a person.\n"
            + "Parameters: PERSON_INDEX\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS_EMPTY = "%s has no payments.";
    public static final String MESSAGE_SUCCESS = "Payments for %s (%d):\n%s";

    private static final Logger logger = LogsCenter.getLogger(ViewPaymentCommand.class);

    private final Index index; // 1-based

    public ViewPaymentCommand(Index index) {
        requireNonNull(index);
        this.index = index;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        logger.fine("ViewPaymentCommand.execute start index=" + index);

        List<Person> lastShownList = model.getFilteredPersonList();
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person person = lastShownList.get(index.getZeroBased());
        List<Payment> payments = person.getPayments();

        if (payments.isEmpty()) {
            return new CommandResult(String.format(MESSAGE_SUCCESS_EMPTY, person.getName()));
        }

        String lines = IntStream.range(0, payments.size())
                .mapToObj(i -> "p/" + (i + 1) + ": " + formatPayment(payments.get(i)))
                .collect(Collectors.joining("\n"));

        String msg = String.format(MESSAGE_SUCCESS, person.getName(), payments.size(), lines);
        logger.fine("ViewPaymentCommand.execute done count=" + payments.size());
        return new CommandResult(msg);
    }

    private static String formatPayment(Payment p) {
        String amt = p.getAmount().toString();
        String date = p.getDate().toString();
        String remarks = p.getRemarks();
        if (remarks == null || remarks.isBlank()) {
            return amt + " on " + date;
        }
        return amt + " on " + date + " — " + remarks;
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof ViewPaymentCommand
                && index.equals(((ViewPaymentCommand) other).index));
    }
}
