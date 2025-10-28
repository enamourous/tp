package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PAYMENT_AMOUNT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PAYMENT_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PAYMENT_REMARKS;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddPaymentCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.payment.Amount;

/**
 * Parses input arguments and creates a new {@code AddPaymentCommand} object.
 * <p>
 * Supports flexible date formats (yyyy-MM-dd or yyyy-M-d), ensures no duplicate indexes
 * or prefixes, and rejects invalid or future dates.
 */
public class AddPaymentCommandParser implements Parser<AddPaymentCommand> {

    public static final String MESSAGE_INVALID_AMOUNT =
        "Amount must be positive and have at most 2 decimal places!";
    public static final String MESSAGE_INVALID_DATE =
        "Invalid date. Please use YYYY-MM-DD or YYYY-M-D, and ensure the date is not in the future.";

    @Override
    public AddPaymentCommand parse(String args) throws ParseException {
        ArgumentMultimap map = ArgumentTokenizer.tokenize(
            args, PREFIX_PAYMENT_AMOUNT, PREFIX_PAYMENT_DATE, PREFIX_PAYMENT_REMARKS);

        // Step 1: Validate prefix usage
        validatePrefixes(map);

        // Step 2: Parse indexes from preamble
        List<Index> indexes = parseIndexes(map.getPreamble());

        // Step 3: Parse values (amount, date, remarks)
        Amount amount = parseAmount(map.getValue(PREFIX_PAYMENT_AMOUNT).get());
        LocalDate date = parseDate(map.getValue(PREFIX_PAYMENT_DATE).get());
        String remarks = map.getValue(PREFIX_PAYMENT_REMARKS).orElse(null);

        // Step 4: Build and return command
        return new AddPaymentCommand(indexes, amount, date, remarks);
    }

    // ----------------------------------------------------
    // Helper methods
    // ----------------------------------------------------

    /**
     * Validates required and duplicate prefixes.
     *
     * @throws ParseException if required prefixes are missing or duplicates exist
     */
    private void validatePrefixes(ArgumentMultimap map) throws ParseException {
        // Reject duplicate prefixes
        map.verifyNoDuplicatePrefixesFor(
            PREFIX_PAYMENT_AMOUNT, PREFIX_PAYMENT_DATE, PREFIX_PAYMENT_REMARKS);

        // Require a preamble (index) and mandatory amount/date prefixes
        if (map.getPreamble().isBlank()
            || !arePrefixesPresent(map, PREFIX_PAYMENT_AMOUNT, PREFIX_PAYMENT_DATE)) {
            throw new ParseException(String.format(
                MESSAGE_INVALID_COMMAND_FORMAT, AddPaymentCommand.MESSAGE_USAGE));
        }
    }

    /**
     * Parses and validates the list of indexes from the preamble.
     *
     * @param preamble the raw string before the prefixes (e.g. "1,2,3")
     * @return a list of unique Index objects
     * @throws ParseException if any index is invalid or duplicated
     */
    private List<Index> parseIndexes(String preamble) throws ParseException {
        String[] indexTokens = preamble.split(",");
        List<Index> indexes = new ArrayList<>();

        for (String token : indexTokens) {
            String trimmed = token.trim();
            if (!trimmed.isEmpty()) {
                indexes.add(ParserUtil.parseIndex(trimmed));
            }
        }

        if (indexes.isEmpty()) {
            throw new ParseException(String.format(
                MESSAGE_INVALID_COMMAND_FORMAT, AddPaymentCommand.MESSAGE_USAGE));
        }

        // Reject duplicate indexes
        long uniqueCount = indexes.stream()
            .map(Index::getZeroBased)
            .distinct()
            .count();

        if (indexes.size() != uniqueCount) {
            throw new ParseException("Duplicate indexes detected. Each index must be unique.");
        }

        return indexes;
    }

    /**
     * Parses a valid Amount object from the given string.
     *
     * @throws ParseException if amount format is invalid
     */
    private Amount parseAmount(String amountStr) throws ParseException {
        try {
            return Amount.parse(amountStr);
        } catch (IllegalArgumentException ex) {
            throw new ParseException(MESSAGE_INVALID_AMOUNT, ex);
        }
    }

    /**
     * Parses a valid LocalDate from the given string.
     * <p>
     * Accepts both yyyy-MM-dd and yyyy-M-d formats, rejecting future dates.
     *
     * @throws ParseException if date format is invalid or in the future
     */
    private LocalDate parseDate(String dateStr) throws ParseException {
        try {
            return seedu.address.model.payment.Payment.parseFlexibleDate(dateStr);
        } catch (DateTimeParseException | IllegalArgumentException ex) {
            throw new ParseException(MESSAGE_INVALID_DATE, ex);
        }
    }

    /**
     * Returns true if all specified prefixes are present in the map.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
