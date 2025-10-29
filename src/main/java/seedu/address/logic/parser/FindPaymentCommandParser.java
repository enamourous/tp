package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.FindPaymentCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.payment.Amount;

/**
 * Parses input arguments and creates a new FindPaymentCommand object.
 */
public class FindPaymentCommandParser implements Parser<FindPaymentCommand> {

    private static final Prefix PREFIX_AMOUNT = new Prefix("a/");
    private static final Prefix PREFIX_REMARK = new Prefix("r/");
    private static final Prefix PREFIX_DATE = new Prefix("d/");

    // Immutable error messages
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

    @Override
    public FindPaymentCommand parse(String args) throws ParseException {
        // Check for unknown prefixes
        String[] parts = args.trim().split("\\s+");
        System.out.println(Arrays.toString(parts));
        for (int i = 1; i < parts.length; i++) { // skip preamble (index) at parts[0]
            String part = parts[i];
            if (!(part.startsWith(PREFIX_AMOUNT.getPrefix())
                    || part.startsWith(PREFIX_REMARK.getPrefix())
                    || part.startsWith(PREFIX_DATE.getPrefix()))) {
                throw new ParseException("Unknown tag: " + part);
            }
        }

        ArgumentMultimap map = ArgumentTokenizer.tokenize(args, PREFIX_AMOUNT, PREFIX_REMARK, PREFIX_DATE);

        if (map.getPreamble().isBlank()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    FindPaymentCommand.MESSAGE_USAGE));
        }

        Index index = ParserUtil.parseIndex(map.getPreamble());

        int filtersUsed = countFilters(map);
        checkFilterCount(filtersUsed);

        return parseFilter(map, index);
    }



    private int countFilters(ArgumentMultimap map) {
        return (map.getValue(PREFIX_AMOUNT).isPresent() ? 1 : 0)
                + (map.getValue(PREFIX_REMARK).isPresent() ? 1 : 0)
                + (map.getValue(PREFIX_DATE).isPresent() ? 1 : 0);
    }

    private void checkFilterCount(int filtersUsed) throws ParseException {
        if (filtersUsed == 0) {
            throw new ParseException(MESSAGE_MISSING_FILTER);
        }
        if (filtersUsed > 1) {
            throw new ParseException(MESSAGE_TOO_MANY_FILTERS);
        }
    }

    private FindPaymentCommand parseFilter(ArgumentMultimap map, Index index) throws ParseException {
        Optional<String> amountVal = map.getValue(PREFIX_AMOUNT);
        Optional<String> remarkVal = map.getValue(PREFIX_REMARK);
        Optional<String> dateVal = map.getValue(PREFIX_DATE);

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
