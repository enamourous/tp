package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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

    @Override
    public FindPaymentCommand parse(String args) throws ParseException {
        ArgumentMultimap map = ArgumentTokenizer.tokenize(args, PREFIX_AMOUNT, PREFIX_REMARK, PREFIX_DATE);

        if (map.getPreamble().isBlank()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    FindPaymentCommand.MESSAGE_USAGE));
        }

        Index index = ParserUtil.parseIndex(map.getPreamble());
        Optional<String> amountVal = map.getValue(PREFIX_AMOUNT);
        Optional<String> remarkVal = map.getValue(PREFIX_REMARK);
        Optional<String> dateVal = map.getValue(PREFIX_DATE);

        int filtersUsed = (amountVal.isPresent() ? 1 : 0)
                + (remarkVal.isPresent() ? 1 : 0)
                + (dateVal.isPresent() ? 1 : 0);

        if (filtersUsed == 0) {
            throw new ParseException("Please provide one filter: a/AMOUNT, d/DATE or r/REMARK");
        }
        if (filtersUsed > 1) {
            throw new ParseException("Please specify only one filter at a time.");
        }

        Amount amount = null;
        String remark = null;
        LocalDate date = null;

        if (amountVal.isPresent()) {
            try {
                amount = Amount.parse(amountVal.get());
            } catch (IllegalArgumentException ex) {
                throw new ParseException("Invalid amount: must be positive and ≤ 2 decimal places.", ex);
            }
        } else if (remarkVal.isPresent()) {
            remark = remarkVal.get().trim();
            if (remark.isEmpty()) {
                throw new ParseException("Remark cannot be empty.");
            }
        } else {
            try {
                date = LocalDate.parse(dateVal.get().trim());
            } catch (DateTimeParseException ex) {
                throw new ParseException("Invalid date format. Please use YYYY-MM-DD.", ex);
            }
        }

        return new FindPaymentCommand(index, amount, remark, date);
    }
}
