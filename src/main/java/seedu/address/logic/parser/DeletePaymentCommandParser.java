package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeletePaymentCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments of the form:
 *   PERSON_INDEX p/PAYMENT_INDEX[,PAYMENT_INDEX]...
 * e.g. "2 p/1,2,3"
 */
public class DeletePaymentCommandParser implements Parser<DeletePaymentCommand> {

    public static final String MESSAGE_DUPLICATE_PAYMENT_INDEX =
            "Duplicate payment indexes are not allowed.";

    @Override
    public DeletePaymentCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, CliSyntax.PREFIX_PAYMENT_INDEX);
        argMultimap.verifyNoDuplicatePrefixesFor(CliSyntax.PREFIX_PAYMENT_INDEX);

        // PERSON_INDEX in preamble (exactly one!!!)
        final String preamble = argMultimap.getPreamble().trim();
        final Index personIndex;
        try {
            // single index now
            personIndex = ParserUtil.parseIndex(preamble);
        } catch (ParseException pe) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, DeletePaymentCommand.MESSAGE_USAGE));
        }

        // after p/
        final String paymentsRaw = argMultimap
                .getValue(CliSyntax.PREFIX_PAYMENT_INDEX)
                .orElse("")
                .trim();

        if (paymentsRaw.isEmpty()) {
            throw new ParseException("Missing payment index after 'p/'. Example: p/1,2,3");
        }

        final String[] tokens = paymentsRaw.split(",", -1);
        final List<Index> paymentIndexes = new ArrayList<>();

        for (String token : tokens) {
            final String t = token.trim();
            if (t.isEmpty()) {
                throw new ParseException(
                        "Empty payment indexes are not allowed. Remove stray commas/spaces. Example: p/1,2,3");
            }
            paymentIndexes.add(ParserUtil.parseIndex(t));
        }

        if (paymentIndexes.isEmpty()) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, DeletePaymentCommand.MESSAGE_USAGE));
        }

        final java.util.Set<Integer> seen = new java.util.HashSet<>();
        for (Index idx : paymentIndexes) {
            int oneBased = idx.getOneBased();
            if (!seen.add(oneBased)) {
                throw new ParseException(MESSAGE_DUPLICATE_PAYMENT_INDEX);
            }
        }

        return new DeletePaymentCommand(personIndex, paymentIndexes);
    }
}
