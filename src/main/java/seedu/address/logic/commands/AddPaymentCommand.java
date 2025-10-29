package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.Messages;
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

    private static final Logger logger = LogsCenter.getLogger(AddPaymentCommand.class);

    private final List<Index> indexes;
    private final Amount amount;
    private final LocalDate date;
    private final String remarks;

    /**
     * Constructs an {@code AddPaymentCommand} to add a payment to one or more persons.
     *
     * @param indexes target person indexes (as displayed).
     * @param amount payment amount (non-null).
     * @param date payment date (non-null).
     * @param remarks optional remarks (nullable).
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
        logger.fine("Executing AddPaymentCommand with " + indexes.size() + " target(s).");

        List<Person> displayedList = model.getFilteredPersonList();
        Payment payment = createPayment(amount, date, remarks);

        List<String> updatedNames = addPaymentToTargets(model, displayedList, indexes, payment);

        String message = buildSuccessMessage(payment, updatedNames);
        logger.info("Successfully added payment: $" + payment + " to " + String.join(", ", updatedNames));

        return new CommandResult(message);
    }

    // ---------------------------------------------------------------------
    // Helper methods
    // ---------------------------------------------------------------------

    /**
     * Creates a {@link Payment} instance. Arguments are assumed validated upstream.
     */
    private Payment createPayment(Amount amount, LocalDate date, String remarks) {
        assert amount != null : "amount must not be null";
        assert date != null : "date must not be null";
        return new Payment(amount, date, remarks);
    }

    /**
     * Adds the given {@code payment} to each person referenced by {@code indexes} in {@code displayedList},
     * mutating the model via {@link Model#setPerson(Person, Person)}. Preserves input order and fails fast
     * on the first invalid index, matching original behavior.
     *
     * @return list of updated person names in the order processed.
     * @throws CommandException if any index is out of bounds.
     */
    private List<String> addPaymentToTargets(
            Model model,
            List<Person> displayedList,
            List<Index> indexes,
            Payment payment) throws CommandException {

        List<String> updatedNames = new ArrayList<>();
        for (Index idx : indexes) {
            Person target = getPersonOrThrow(displayedList, idx);
            logger.fine("Adding payment to: " + target.getName());

            Person updated = target.withAddedPayment(payment);
            model.setPerson(target, updated);
            updatedNames.add(updated.getName().toString());
        }
        return updatedNames;
    }

    /**
     * Returns the person at the given index from {@code displayedList} or throws a {@link CommandException}
     * using {@link Messages#MESSAGE_INVALID_PERSON_DISPLAYED_INDEX} if out of bounds. Logs a warning on failure.
     */
    private Person getPersonOrThrow(List<Person> displayedList, Index index) throws CommandException {
        int zeroBased = index.getZeroBased();
        if (zeroBased >= displayedList.size()) {
            logger.warning("Invalid person index: " + index.getOneBased());
            throw new CommandException(MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
        return displayedList.get(zeroBased);
    }

    /**
     * Builds the success message using {@link #MESSAGE_SUCCESS_TEMPLATE}, preserving original wording.
     */
    private String buildSuccessMessage(Payment payment, List<String> updatedNames) {
        String joinedNames = String.join(", ", updatedNames);
        return String.format(MESSAGE_SUCCESS_TEMPLATE, payment, joinedNames);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof AddPaymentCommand
                && indexes.equals(((AddPaymentCommand) other).indexes)
                && amount.equals(((AddPaymentCommand) other).amount)
                && date.equals(((AddPaymentCommand) other).date)
                && java.util.Objects.equals(remarks, ((AddPaymentCommand) other).remarks)); // remarks could be null
    }

    @Override
    public boolean isMutating() {
        return true;
    }
}
