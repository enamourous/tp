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
 *
 * Accepts exactly one filter (a/, d/, or r/) and rejects unknown prefixes.
 */
public class FindPaymentCommandParser implements Parser<FindPaymentCommand> {

    private static final String MESSAGE_MISSING_FILTER =
            "Please provide one filter: a/AMOUNT, d/DATE or r/REMARK";
    private static final String MESSAGE_TOO_MANY_FILTERS =
            "Please specify only one filter at a time.";
    private static final String MESSAGE_INVALID_AMOUNT =
            "Invalid amount: must be positive and ≤ 2 decimal places.";
    private static final String MESSAGE_INVALID_DATE =
            "Invalid date format. Please use YYYY-MM-DD.";
    private static final String MESSAGE_EMPTY_REMARK =
            "Remark cannot be empty.";
    private static final String MESSAGE_UNKNOWN_PREFIX =
            "Unknown prefix: %s";

    // Allowed prefixes
    private static final String[] VALID_PREFIXES = { "a/", "r/", "d/" };

    @Override
    public FindPaymentCommand parse(String args) throws ParseException {
        // reject any unknown prefix in the input
        checkForUnknownPrefixes(args);

        ArgumentMultimap map = ArgumentTokenizer.tokenize(args,
                PREFIX_PAYMENT_AMOUNT, PREFIX_PAYMENT_REMARKS, PREFIX_PAYMENT_DATE);

        // validate index
        if (map.getPreamble().isBlank()) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, FindPaymentCommand.MESSAGE_USAGE));
        }
        Index index = ParserUtil.parseIndex(map.getPreamble());

        // only allow exactly one filter
        map.verifyNoDuplicatePrefixesFor(PREFIX_PAYMENT_AMOUNT, PREFIX_PAYMENT_REMARKS, PREFIX_PAYMENT_DATE);
        int filtersUsed = countFilters(map);
        if (filtersUsed == 0) {
            throw new ParseException(MESSAGE_MISSING_FILTER);
        }
        if (filtersUsed > 1) {
            throw new ParseException(MESSAGE_TOO_MANY_FILTERS);
        }

        // Step 5: Parse the chosen filter
        return parseFilter(map, index);
    }

    // ----------------------------------------------------
    // Validation helpers
    // ----------------------------------------------------

    /**
     * Scans for unknown prefixes like x/, y/, etc. and rejects them.
     */
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

    /**
     * Counts how many valid filter prefixes (a/, r/, d/) are present.
     */
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

    private Amount parseAmount(String value) throws ParseException {
        try {
            return Amount.parse(value);
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

    private LocalDate parseDate(String value) throws ParseException {
        try {
            return LocalDate.parse(value.trim());
        } catch (DateTimeParseException ex) {
            throw new ParseException(MESSAGE_INVALID_DATE, ex);
        }
    }
}
