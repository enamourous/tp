package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

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
 * Parses input arguments into an AddPaymentCommand.
 */
public class AddPaymentCommandParser implements Parser<AddPaymentCommand> {

    public static final String MESSAGE_INVALID_AMOUNT =
            "Amount must be positive and have at most 2 decimal places!";
    public static final String MESSAGE_INVALID_DATE =
            "Invalid date format. Please use YYYY-MM-DD.";

    private static final Prefix PREFIX_AMOUNT = new Prefix("a/");
    private static final Prefix PREFIX_DATE = new Prefix("d/");
    private static final Prefix PREFIX_REMARKS = new Prefix("r/");

    @Override
    public AddPaymentCommand parse(String args) throws ParseException {
        ArgumentMultimap map = ArgumentTokenizer.tokenize(args, PREFIX_AMOUNT, PREFIX_DATE, PREFIX_REMARKS);

        // require: index preamble + a/ + d/
        if (map.getPreamble().isBlank() || !arePrefixesPresent(map, PREFIX_AMOUNT, PREFIX_DATE)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddPaymentCommand.MESSAGE_USAGE));
        }

        try {
            // Parse comma-separated indexes
            String[] indexTokens = map.getPreamble().split(",");
            List<Index> indexes = new ArrayList<>();

            for (String token : indexTokens) {
                token = token.trim();
                if (!token.isEmpty()) {
                    indexes.add(ParserUtil.parseIndex(token));
                }
            }

            // technically this branch should not happen since the first branch has already handled that,
            // left it here for safety
            if (indexes.isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        AddPaymentCommand.MESSAGE_USAGE));
            }

            // Parse values
            String amountStr = map.getValue(PREFIX_AMOUNT).get();
            String dateStr = map.getValue(PREFIX_DATE).get();
            String remarks = map.getValue(PREFIX_REMARKS).orElse(null);

            Amount amount;
            LocalDate date;

            // handle bad amount
            try {
                amount = Amount.parse(amountStr);
            } catch (IllegalArgumentException ex) {
                throw new ParseException(MESSAGE_INVALID_AMOUNT, ex);
            }

            // handle bad date
            try {
                date = LocalDate.parse(dateStr);
            } catch (DateTimeParseException ex) {
                throw new ParseException(MESSAGE_INVALID_DATE, ex);
            }

            // Build command
            return new AddPaymentCommand(indexes, amount, date, remarks);

        } catch (ParseException pe) {
            throw pe;

        } catch (Exception e) {
            // fallback for anything unexpected
            // current test does not cover this branch, left it here for safety
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddPaymentCommand.MESSAGE_USAGE), e);
        }
    }

    /**
     * Returns true if none of the prefixes are missing their values in the given {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
