package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PAYMENT_AMOUNT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PAYMENT_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PAYMENT_REMARKS;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.FindPaymentCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.payment.Amount;

/**
 * Parses input arguments and creates a new FindPaymentCommand object.
 * <p>
 * Behaves consistently with AddPaymentCommandParser: provides clear usage
 * feedback for invalid formats and rejects unknown prefixes.
 */
public class FindPaymentCommandParser implements Parser<FindPaymentCommand> {

    private static final String MESSAGE_MISSING_FILTER =
            "Please provide one filter: a/AMOUNT, d/DATE or r/REMARK";
    private static final String MESSAGE_TOO_MANY_FILTERS =
            "Please specify only one filter at a time.";
    private static final String MESSAGE_INVALID_AMOUNT =
            "Invalid amount: must be positive and ≤ 2 decimal places.";
    private static final String MESSAGE_INVALID_DATE =
            "Invalid date. Please use YYYY-MM-DD or YYYY-M-D, and ensure the date is not in the future.";
    private static final String MESSAGE_EMPTY_REMARK =
            "Remark cannot be empty.";
    private static final String MESSAGE_UNKNOWN_PREFIX =
            "Unknown filter: %s (valid filters are a/AMOUNT, d/DATE and r/REMARK)";

    private static final String[] VALID_PREFIXES = { "a/", "r/", "d/" };

    @Override
    public FindPaymentCommand parse(String args) throws ParseException {
        ArgumentMultimap map = ArgumentTokenizer.tokenize(
                args, PREFIX_PAYMENT_AMOUNT, PREFIX_PAYMENT_REMARKS, PREFIX_PAYMENT_DATE);

        checkForUnknownPrefixes(args);

        String preamble = map.getPreamble().trim();
        if (preamble.isBlank()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindPaymentCommand.MESSAGE_USAGE));
        }

        Index index;
        try {
            // only take the first word before any spaces
            String[] tokens = preamble.split("\\s+");
            if (tokens.length > 1) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        FindPaymentCommand.MESSAGE_USAGE));
            }
            index = ParserUtil.parseIndex(tokens[0]);
        } catch (Exception e) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindPaymentCommand.MESSAGE_USAGE));
        }

        map.verifyNoDuplicatePrefixesFor(PREFIX_PAYMENT_AMOUNT, PREFIX_PAYMENT_REMARKS, PREFIX_PAYMENT_DATE);

        int filtersUsed = countFilters(map);
        if (filtersUsed == 0) {
            throw new ParseException(MESSAGE_MISSING_FILTER);
        }
        if (filtersUsed > 1) {
            throw new ParseException(MESSAGE_TOO_MANY_FILTERS);
        }

        return parseFilter(map, index);
    }

    // ----------------------------------------------------
    // Validation helpers
    // ----------------------------------------------------

    private void checkForUnknownPrefixes(String args) throws ParseException {
        Matcher matcher = Pattern.compile("\\b([a-zA-Z]{1,5}/)").matcher(args);
        while (matcher.find()) {
            String prefix = matcher.group(1);
            boolean isKnown = Arrays.stream(VALID_PREFIXES).anyMatch(prefix::equals);
            if (!isKnown) {
                throw new ParseException(String.format(MESSAGE_UNKNOWN_PREFIX, prefix));
            }
        }
    }

    private int countFilters(ArgumentMultimap map) {
        return (map.getValue(PREFIX_PAYMENT_AMOUNT).isPresent() ? 1 : 0)
                + (map.getValue(PREFIX_PAYMENT_REMARKS).isPresent() ? 1 : 0)
                + (map.getValue(PREFIX_PAYMENT_DATE).isPresent() ? 1 : 0);
    }

    // ----------------------------------------------------
    // Parsing logic
    // ----------------------------------------------------

    private FindPaymentCommand parseFilter(ArgumentMultimap map, Index index) throws ParseException {
        Optional<String> amountVal = map.getValue(PREFIX_PAYMENT_AMOUNT);
        Optional<String> remarkVal = map.getValue(PREFIX_PAYMENT_REMARKS);
        Optional<String> dateVal = map.getValue(PREFIX_PAYMENT_DATE);

        if (amountVal.isPresent()) {
            Amount amount = parseAmount(amountVal.get());
            return new FindPaymentCommand(index, amount, null, null);
        } else if (remarkVal.isPresent()) {
            String remark = parseRemark(remarkVal.get());
            return new FindPaymentCommand(index, null, remark, null);
        } else {
            LocalDate date = parseDate(dateVal.get());
            return new FindPaymentCommand(index, null, null, date);
        }
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

    private String parseRemark(String value) throws ParseException {
        String remark = value.trim();
        if (remark.isEmpty()) {
            throw new ParseException(MESSAGE_EMPTY_REMARK);
        }
        return remark;
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
}
