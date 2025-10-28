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
 * Parses input arguments into an AddPaymentCommand.
 */
public class AddPaymentCommandParser implements Parser<AddPaymentCommand> {

    public static final String MESSAGE_INVALID_AMOUNT =
            "Amount must be positive and have at most 2 decimal places!";
    public static final String MESSAGE_INVALID_DATE =
        "Invalid date. Please use YYYY-MM-DD or YYYY-M-D, and ensure the date is not in the future.";


    @Override
    public AddPaymentCommand parse(String args) throws ParseException {
        ArgumentMultimap map = ArgumentTokenizer.tokenize(args,
            PREFIX_PAYMENT_AMOUNT, PREFIX_PAYMENT_DATE, PREFIX_PAYMENT_REMARKS);

        // require: index preamble + a/ + d/
        if (map.getPreamble().isBlank() || !arePrefixesPresent(map, PREFIX_PAYMENT_AMOUNT, PREFIX_PAYMENT_DATE)) {
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

            // 🚫 Reject duplicate indexes
            long uniqueCount = indexes.stream()
                .map(Index::getZeroBased) // compare by integer value
                .distinct()
                .count();

            if (indexes.size() != uniqueCount) {
                throw new ParseException("Duplicate indexes detected. Each index must be unique.");
            }


            // technically this branch should not happen since the first branch has already handled that,
            // left it here for safety
            if (indexes.isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddPaymentCommand.MESSAGE_USAGE));
            }


            // Parse values
            String amountStr = map.getValue(PREFIX_PAYMENT_AMOUNT).get();
            String dateStr = map.getValue(PREFIX_PAYMENT_DATE).get();
            String remarks = map.getValue(PREFIX_PAYMENT_REMARKS).orElse(null);

            Amount amount;
            LocalDate date;

            // handle bad amount
            try {
                amount = Amount.parse(amountStr);
            } catch (IllegalArgumentException ex) {
                throw new ParseException(MESSAGE_INVALID_AMOUNT, ex);
            }

            // handle bad date (flexible parsing)
            try {
                date = seedu.address.model.payment.Payment.parseFlexibleDate(dateStr);
            } catch (DateTimeParseException | IllegalArgumentException ex) {
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
