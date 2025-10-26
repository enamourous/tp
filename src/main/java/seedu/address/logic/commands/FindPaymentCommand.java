package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
            + "Example: " + COMMAND_WORD + " 1 r/CCA";

    public static final String MESSAGE_SUCCESS = "Found %d payment(s) for %s:\n%s";
    public static final String MESSAGE_NOT_FOUND = "No payments found for %s matching %s.";

    private static final Logger logger = LogsCenter.getLogger(FindPaymentCommand.class);

    private final Index targetIndex;
    private final Amount amount;
    private final String remark;
    private final LocalDate date;

    /**
     * Creates a {@code FindPaymentCommand} to find payments for a specific member
     * identified by their index in the displayed list, filtered by exactly one of
     * the following criteria:
     * <ul>
     *     <li>Amount — matches payments with the exact specified amount</li>
     *     <li>Remark — matches payments whose remark contains the given keyword (case-insensitive)</li>
     *     <li>Date — matches payments made on the specified date</li>
     * </ul>
     *
     * <p>Only one of {@code amount}, {@code remark}, or {@code date} should be non-null.
     * This constraint is enforced by {@link seedu.address.logic.parser.FindPaymentCommandParser}.</p>
     *
     * @param targetIndex the index of the member in the current displayed list
     * @param amount the amount to match payments against, or {@code null} if not filtering by amount
     * @param remark the keyword to match within payment remarks, or {@code null} if not filtering by remark
     * @param date the date to match payments against, or {@code null} if not filtering by date
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
        List<Person> persons = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= persons.size()) {
            throw new CommandException(MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person target = persons.get(targetIndex.getZeroBased());
        List<Payment> payments = target.getPayments();
        List<Payment> matched = findMatchingPayments(payments);

        if (matched.isEmpty()) {
            String criteria = amount != null ? "amount " + amount
                    : date != null ? "date " + date
                    : "remark \"" + remark + "\"";
            return new CommandResult(String.format(MESSAGE_NOT_FOUND, target.getName(), criteria));
        }

        String formattedList = formatPayments(matched);
        return new CommandResult(String.format(MESSAGE_SUCCESS, matched.size(), target.getName(), formattedList));
    }

    private List<Payment> findMatchingPayments(List<Payment> payments) {
        if (amount != null) {
            return payments.stream()
                    .filter(p -> p.getAmount().equals(amount))
                    .collect(Collectors.toList());
        } else if (remark != null) {
            String keyword = remark.toLowerCase();
            return payments.stream()
                    .filter(p -> p.getRemarks() != null
                            && p.getRemarks().toLowerCase().contains(keyword))
                    .collect(Collectors.toList());
        } else if (date != null) {
            return payments.stream()
                    .filter(p -> p.getDate().equals(date))
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    private String formatPayments(List<Payment> payments) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < payments.size(); i++) {
            sb.append(String.format("%d. %s%n", i + 1, payments.get(i)));
        }
        return sb.toString().trim();
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
                && java.util.Objects.equals(amount, o.amount)
                && java.util.Objects.equals(remark, o.remark)
                && java.util.Objects.equals(date, o.date);
    }

    @Override
    public boolean isMutating() {
        return false;
    }
}
