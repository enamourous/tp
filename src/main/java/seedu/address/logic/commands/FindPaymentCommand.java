package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;

import java.time.LocalDate;
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
 * Finds payments of a person by amount, remark, or date.
 */
public class FindPaymentCommand extends Command {

    public static final String COMMAND_WORD = "findpayment";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Finds payments of the person identified by the displayed index, "
            + "filtered by amount, remark, or date.\n"
            + "Parameters: INDEX [a/AMOUNT | r/REMARK | d/DATE]\n"
            + "Example 1: " + COMMAND_WORD + " 1 a/23.00\n"
            + "Example 2: " + COMMAND_WORD + " 1 r/cca\n"
            + "Example 3: " + COMMAND_WORD + " 1 d/2023-02-07";

    public static final String MESSAGE_SUCCESS_AMOUNT = "Payments with amount %s found for %s:\n%s";
    public static final String MESSAGE_SUCCESS_REMARK = "Payments with remark containing \"%s\" found for %s:\n%s";
    public static final String MESSAGE_SUCCESS_DATE = "Payments on date %s found for %s:\n%s";
    public static final String MESSAGE_NOT_FOUND = "No payments found for the given filter.";

    private static final Logger logger = LogsCenter.getLogger(FindPaymentCommand.class);

    private final Index targetIndex;
    private final Amount amount;
    private final String remark;
    private final LocalDate date;

    /**
     * Constructs a {@code FindPaymentCommand} with exactly one non-null filter.
     * Note: when correctly used, only one of the three arguments (amount, remark, date) would be non-null.
     * The above check is done by FindPaymentCommandParser
     */
    public FindPaymentCommand(Index targetIndex, Amount amount, String remark, LocalDate date) {
        requireNonNull(targetIndex);
        this.targetIndex = targetIndex;
        this.amount = amount;
        this.remark = remark;
        this.date = date;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        var list = model.getFilteredPersonList();
        if (targetIndex.getZeroBased() >= list.size()) {
            throw new CommandException(MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person target = list.get(targetIndex.getZeroBased());
        logger.fine("Searching payments for: " + target.getName());

        List<Payment> payments = target.getPayments();
        List<Payment> matched;

        if (amount != null) {
            matched = payments.stream()
                    .filter(p -> p.getAmount().equals(amount))
                    .toList();

        } else if (remark != null) {
            String keyword = remark.toLowerCase();
            matched = payments.stream()
                    .filter(p -> p.getRemarks() != null
                            && p.getRemarks().toLowerCase().contains(keyword))
                    .toList();

        } else if (date != null) {
            matched = payments.stream()
                    .filter(p -> p.getDate().equals(date))
                    .toList();

        } else {
            throw new CommandException("Invalid search.");
        }

        if (matched.isEmpty()) {
            return new CommandResult(MESSAGE_NOT_FOUND);
        }

        StringBuilder sb = new StringBuilder();
        for (Payment p : matched) {
            sb.append("- ").append(p).append("\n");
        }

        String message;
        if (amount != null) {
            message = String.format(MESSAGE_SUCCESS_AMOUNT, amount, target.getName(), sb.toString().trim());
        } else if (remark != null) {
            message = String.format(MESSAGE_SUCCESS_REMARK, remark, target.getName(), sb.toString().trim());
        } else {
            message = String.format(MESSAGE_SUCCESS_DATE, date, target.getName(), sb.toString().trim());
        }

        logger.info("Found " + matched.size() + " matching payments for " + target.getName());
        return new CommandResult(message);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof FindPaymentCommand)) {
            return false;
        }
        FindPaymentCommand o = (FindPaymentCommand) other;
        return targetIndex.equals(o.targetIndex)
                && java.util.Objects.equals(amount, o.amount) // amount could be null
                && java.util.Objects.equals(remark, o.remark) // remark could be null
                && java.util.Objects.equals(date, o.date); // date could be null
    }

    @Override
    public boolean isMutating() {
        return false;
    }
}
