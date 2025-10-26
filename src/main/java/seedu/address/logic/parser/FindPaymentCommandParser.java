package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.FindPaymentCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.payment.Amount;

/**
 * Parses input arguments and creates a new FindPaymentCommand object.
 */
public class FindPaymentCommandParser implements Parser<FindPaymentCommand> {

    public static final String MESSAGE_INVALID_AMOUNT =
            "Amount must be positive and have at most 2 decimal places!";
    public static final String MESSAGE_INVALID_DATE =
            "Invalid date format. Please use YYYY-MM-DD.";
    public static final String MESSAGE_MISSING_FILTER =
            "Please provide one filter: a/AMOUNT, d/DATE or r/REMARK";
    public static final String MESSAGE_TOO_MANY_FILTERS =
            "Please specify only one filter: either a/AMOUNT, d/DATE or r/REMARK";

    private static final Prefix PREFIX_AMOUNT = new Prefix("a/");
    private static final Prefix PREFIX_REMARK = new Prefix("r/");
    private static final Prefix PREFIX_DATE = new Prefix("d/");

    @Override
    public FindPaymentCommand parse(String args) throws ParseException {
        ArgumentMultimap map = ArgumentTokenizer.tokenize(args, PREFIX_AMOUNT, PREFIX_REMARK, PREFIX_DATE);

        if (map.getPreamble().isBlank()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    FindPaymentCommand.MESSAGE_USAGE));
        }

        Index index;
        try {
            index = ParserUtil.parseIndex(map.getPreamble());
        } catch (ParseException e) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    FindPaymentCommand.MESSAGE_USAGE), e);
        }

        boolean hasAmount = map.getValue(PREFIX_AMOUNT).isPresent();
        boolean hasRemark = map.getValue(PREFIX_REMARK).isPresent();
        boolean hasDate = map.getValue(PREFIX_DATE).isPresent();

        // only accept if the number of filters is exactly 1
        // but with different exception thrown for the case 0 and >1
        int filtersUsed = (hasAmount ? 1 : 0) + (hasRemark ? 1 : 0) + (hasDate ? 1 : 0);
        if (filtersUsed == 0) {
            throw new ParseException(MESSAGE_MISSING_FILTER);
        }
        if (filtersUsed > 1) {
            throw new ParseException(MESSAGE_TOO_MANY_FILTERS);
        }

        Amount amount = null;
        String remark = null;
        LocalDate date = null;

        if (hasAmount) {
            String amtStr = map.getValue(PREFIX_AMOUNT).get();
            try {
                amount = Amount.parse(amtStr);
            } catch (IllegalArgumentException ex) {
                throw new ParseException(MESSAGE_INVALID_AMOUNT, ex);
            }
        } else if (hasRemark) {
            remark = map.getValue(PREFIX_REMARK).get().trim();
            if (remark.isEmpty()) {
                throw new ParseException("Remark cannot be empty.");
            }
        } else {
            String dateStr = map.getValue(PREFIX_DATE).get().trim();
            try {
                date = LocalDate.parse(dateStr);
            } catch (DateTimeParseException ex) {
                throw new ParseException(MESSAGE_INVALID_DATE, ex);
            }
        }

        return new FindPaymentCommand(index, amount, remark, date);
    }
}
