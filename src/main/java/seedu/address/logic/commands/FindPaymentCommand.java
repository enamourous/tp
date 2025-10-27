package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;

import java.time.LocalDate;
import java.util.Comparator;
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
     * Constructs a FindPaymentCommand with exactly one non-null filter.
     *
     * @param targetIndex the index of the member in the displayed list
     * @param amount the amount to filter by, or null
     * @param remark the remark keyword to filter by, or null
     * @param date the date to filter by, or null
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
        Person target = getTargetPerson(model);

        List<Payment> matchedPayments = findMatchingPayments(target.getPayments());

        if (matchedPayments.isEmpty()) {
            return new CommandResult(String.format(MESSAGE_NOT_FOUND, target.getName(), getCriteriaString()));
        }

        String formattedPayments = formatPayments(matchedPayments);
        logger.info(String.format("Found %d payments for %s", matchedPayments.size(), target.getName()));
        return new CommandResult(String.format(MESSAGE_SUCCESS, matchedPayments.size(), target.getName(),
                formattedPayments));
    }

    private Person getTargetPerson(Model model) throws CommandException {
        List<Person> persons = model.getFilteredPersonList();
        if (targetIndex.getZeroBased() >= persons.size()) {
            throw new CommandException(MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
        return persons.get(targetIndex.getZeroBased());
    }

    private List<Payment> findMatchingPayments(List<Payment> payments) {
        if (amount != null) {
            return filterByAmount(payments, amount);
        }
        if (remark != null) {
            return filterByRemark(payments, remark);
        }
        if (date != null) {
            return filterByDate(payments, date);
        }
        return List.of(); // should not happen if parser is correct
    }

    private List<Payment> filterByAmount(List<Payment> payments, Amount amount) {
        return payments.stream()
                .filter(p -> p.getAmount().equals(amount))
                .sorted(Comparator.comparing(Payment::getDate, Comparator.reverseOrder())
                        .thenComparing(Payment::getAmount, Comparator.reverseOrder())
                        .thenComparing(p -> p.getRemarks() == null ? "" : p.getRemarks().toLowerCase()))
                .collect(Collectors.toList());
    }

    private List<Payment> filterByRemark(List<Payment> payments, String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return payments.stream()
                .filter(p -> p.getRemarks() != null && p.getRemarks().toLowerCase().contains(lowerKeyword))
                .sorted(Comparator.comparing(Payment::getDate, Comparator.reverseOrder())
                        .thenComparing(Payment::getAmount, Comparator.reverseOrder())
                        .thenComparing(p -> p.getRemarks() == null ? "" : p.getRemarks().toLowerCase()))
                .collect(Collectors.toList());
    }

    private List<Payment> filterByDate(List<Payment> payments, LocalDate date) {
        return payments.stream()
                .filter(p -> p.getDate().equals(date))
                .sorted(Comparator.comparing(Payment::getDate, Comparator.reverseOrder())
                        .thenComparing(Payment::getAmount, Comparator.reverseOrder())
                        .thenComparing(p -> p.getRemarks() == null ? "" : p.getRemarks().toLowerCase()))
                .collect(Collectors.toList());
    }

    private String formatPayments(List<Payment> payments) {
        return payments.stream()
                .map(p -> "- " + p)
                .collect(Collectors.joining("\n"));
    }

    private String getCriteriaString() {
        if (amount != null) {
            return "amount " + amount;
        }
        if (date != null) {
            return "date " + date;
        }
        return "remark \"" + remark + "\"";
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
