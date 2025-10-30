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
 * Parses input arguments and creates a new {@code FindPaymentCommand} object.
 *
 * <p>Expected format:
 * <pre>
 *     findpayment INDEX [a/AMOUNT | d/DATE | r/REMARK]
 * </pre>
 *
 * Examples:
 * <ul>
 *     <li>{@code findpayment 1 a/23.50}</li>
 *     <li>{@code findpayment 3 d/2023-12-30}</li>
 *     <li>{@code findpayment 4 r/cca shirt}</li>
 * </ul>
 */
public class FindPaymentCommandParser implements Parser<FindPaymentCommand> {

    private static final String MESSAGE_MISSING_FILTER =
            "Please provide one filter: a/AMOUNT, d/DATE or r/REMARK";
    private static final String MESSAGE_TOO_MANY_FILTERS =
            "Please specify only one filter at a time.";
    private static final String MESSAGE_INVALID_AMOUNT =
            "Invalid amount: must be positive and ≤ 2 decimal places.";
    private static final String MESSAGE_INVALID_DATE =
            "Invalid date. Please use YYYY-MM-DD or YYYY-M-D, and ensure the date is correct and not in the future.";
    private static final String MESSAGE_EMPTY_REMARK = "Remark cannot be empty.";
    private static final String MESSAGE_EMPTY_AMOUNT = "Amount cannot be empty.";
    private static final String MESSAGE_EMPTY_DATE = "Date cannot be empty.";
    private static final String MESSAGE_UNKNOWN_PREFIX =
            "Unknown filter: %s (valid filters are a/AMOUNT, d/DATE and r/REMARK)";

    private static final String[] VALID_PREFIXES = { "a/", "r/", "d/" };

    /**
     * Parses the given {@code String} of arguments and returns a {@code FindPaymentCommand}.
     *
     * @param args full user input string.
     * @return a {@code FindPaymentCommand} ready for execution.
     * @throws ParseException if the user input does not conform to the expected format.
     */
    @Override
    public FindPaymentCommand parse(String args) throws ParseException {
        ArgumentMultimap argMap = ArgumentTokenizer.tokenize(
                args, PREFIX_PAYMENT_AMOUNT, PREFIX_PAYMENT_REMARKS, PREFIX_PAYMENT_DATE);

        checkForUnknownPrefixes(args);

        Index targetIndex = parseIndex(argMap);
        validatePrefixUsage(argMap);

        return buildCommand(argMap, targetIndex);
    }

    // ----------------------------------------------------
    // Helpers for parsing and validation
    // ----------------------------------------------------

    /**
     * Extracts and validates the index (person index) from the command preamble.
     *
     * @param map tokenized argument multimap.
     * @return a valid {@code Index}.
     * @throws ParseException if the index is missing, non-numeric, or improperly formatted.
     */
    private Index parseIndex(ArgumentMultimap map) throws ParseException {
        String preamble = map.getPreamble().trim();
        if (preamble.isBlank()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindPaymentCommand.MESSAGE_USAGE));
        }

        String[] tokens = preamble.split("\\s+");
        if (tokens.length != 1) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindPaymentCommand.MESSAGE_USAGE));
        }

        try {
            return ParserUtil.parseIndex(tokens[0]);
        } catch (Exception e) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindPaymentCommand.MESSAGE_USAGE));
        }
    }

    /**
     * Ensures that exactly one valid filter prefix is used (a/, d/, or r/),
     * and that no prefix appears more than once.
     *
     * @param map tokenized argument multimap.
     * @throws ParseException if zero or more than one filters are provided, or duplicates are found.
     */
    private void validatePrefixUsage(ArgumentMultimap map) throws ParseException {
        map.verifyNoDuplicatePrefixesFor(PREFIX_PAYMENT_AMOUNT, PREFIX_PAYMENT_REMARKS, PREFIX_PAYMENT_DATE);

        int filtersUsed = countFilters(map);
        if (filtersUsed == 0) {
            throw new ParseException(MESSAGE_MISSING_FILTER);
        }
        if (filtersUsed > 1) {
            throw new ParseException(MESSAGE_TOO_MANY_FILTERS);
        }
    }

    /**
     * Constructs the appropriate {@code FindPaymentCommand} depending on which
     * filter prefix was provided by the user.
     *
     * @param map   parsed argument multimap.
     * @param index person index to find payments for.
     * @return a fully constructed {@code FindPaymentCommand}.
     * @throws ParseException if any argument value is invalid.
     */
    private FindPaymentCommand buildCommand(ArgumentMultimap map, Index index) throws ParseException {
        Optional<String> amountVal = map.getValue(PREFIX_PAYMENT_AMOUNT);
        Optional<String> remarkVal = map.getValue(PREFIX_PAYMENT_REMARKS);
        Optional<String> dateVal = map.getValue(PREFIX_PAYMENT_DATE);

        if (amountVal.isPresent()) {
            Amount amount = parseAmount(amountVal.get());
            return new FindPaymentCommand(index, amount, null, null);
        }

        if (remarkVal.isPresent()) {
            String remark = parseRemark(remarkVal.get());
            return new FindPaymentCommand(index, null, remark, null);
        }

        LocalDate date = parseDate(dateVal.get());
        return new FindPaymentCommand(index, null, null, date);
    }

    /**
     * Checks the raw argument string for any unrecognized prefixes.
     *
     * @param args full user input string.
     * @throws ParseException if an unknown prefix is detected.
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
     * Counts how many filter prefixes (a/, d/, r/) are present in the parsed argument map.
     *
     * @param map argument multimap to inspect.
     * @return number of filters used.
     */
    private int countFilters(ArgumentMultimap map) {
        return (map.getValue(PREFIX_PAYMENT_AMOUNT).isPresent() ? 1 : 0)
                + (map.getValue(PREFIX_PAYMENT_REMARKS).isPresent() ? 1 : 0)
                + (map.getValue(PREFIX_PAYMENT_DATE).isPresent() ? 1 : 0);
    }

    // ----------------------------------------------------
    // Filter parsers for amount, remark and date
    // ----------------------------------------------------

    /**
     * Parses the amount string into an {@code Amount} object.
     *
     * @param amountStr amount value as string.
     * @return {@code Amount} object representing the value.
     * @throws ParseException if empty, non-positive, or improperly formatted.
     */
    private Amount parseAmount(String amountStr) throws ParseException {
        if (amountStr.trim().isEmpty()) {
            throw new ParseException(MESSAGE_EMPTY_AMOUNT);
        }
        try {
            return Amount.parse(amountStr);
        } catch (IllegalArgumentException ex) {
            throw new ParseException(MESSAGE_INVALID_AMOUNT, ex);
        }
    }

    /**
     * Parses and validates the remark string.
     *
     * @param value user-provided remark string.
     * @return trimmed, non-empty remark.
     * @throws ParseException if remark is blank.
     */
    private String parseRemark(String value) throws ParseException {
        String remark = value.trim();
        if (remark.isEmpty()) {
            throw new ParseException(MESSAGE_EMPTY_REMARK);
        }
        return remark;
    }

    /**
     * Parses and validates the date string.
     *
     * @param dateStr user-provided date string.
     * @return {@code LocalDate} representing the parsed date.
     * @throws ParseException if format is invalid or date is in the future.
     */
    private LocalDate parseDate(String dateStr) throws ParseException {
        if (dateStr.trim().isEmpty()) {
            throw new ParseException(MESSAGE_EMPTY_DATE);
        }
        try {
            return seedu.address.model.payment.Payment.parseFlexibleDate(dateStr);
        } catch (DateTimeParseException | IllegalArgumentException ex) {
            throw new ParseException(MESSAGE_INVALID_DATE, ex);
        }
    }
}
