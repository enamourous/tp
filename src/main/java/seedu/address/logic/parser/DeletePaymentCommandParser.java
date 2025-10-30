package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeletePaymentCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments for:
 *   PERSON_INDEX p/PAYMENT_INDEX[,PAYMENT_INDEX]...
 * e.g. "2 p/1,2,3"
 */
public class DeletePaymentCommandParser implements Parser<DeletePaymentCommand> {

    public static final String MESSAGE_DUPLICATE_PAYMENT_INDEX =
            "Duplicate payment indexes are not allowed.";

    private static final String MESSAGE_MISSING_PAY_INDEX =
            "Missing payment index after 'p/'. Example: p/1,2,3";
    private static final String MESSAGE_EMPTY_PAY_TOKENS =
            "Empty payment indexes are not allowed. Remove stray commas/spaces. Example: p/1,2,3";

    @Override
    public DeletePaymentCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap argMultimap = tokenizeAndVerify(args);

        Index personIndex = parsePersonIndex(argMultimap);
        List<Index> paymentIndexes = parsePaymentIndexes(argMultimap);

        ensureNoDuplicatePaymentIndexes(paymentIndexes);

        return new DeletePaymentCommand(personIndex, paymentIndexes);
    }

    // ------------------------ helpers ------------------------

    /**
     * Tokenizes the raw input string using {@link ArgumentTokenizer} and ensures that
     * the payment index prefix (p/) does not appear more than once.
     *
     * @param args the raw user input arguments
     * @return an {@link ArgumentMultimap} containing the preamble and prefix mappings
     * @throws ParseException if duplicate prefixes are found
     */
    private ArgumentMultimap tokenizeAndVerify(String args) throws ParseException {
        ArgumentMultimap mm = ArgumentTokenizer.tokenize(args, CliSyntax.PREFIX_PAYMENT_INDEX);
        mm.verifyNoDuplicatePrefixesFor(CliSyntax.PREFIX_PAYMENT_INDEX);
        return mm;
    }

    /**
     * Parses and validates the person index from the command preamble.
     * Expects exactly one valid index.
     *
     * @param mm the tokenized argument multimap
     * @return a valid {@link Index} representing the person to edit
     * @throws ParseException if the person index is missing or invalid
     */
    private Index parsePersonIndex(ArgumentMultimap mm) throws ParseException {
        String preamble = mm.getPreamble().trim();
        try {
            return ParserUtil.parseIndex(preamble);
        } catch (ParseException e) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, DeletePaymentCommand.MESSAGE_USAGE));
        }
    }

    /**
     * Parses the list of payment indices from the p/ prefix argument.
     * Comma-separated values are accepted, e.g. {@code p/1,2,3}.
     *
     * @param mm the tokenized argument multimap containing the p/ value
     * @return a list of {@link Index} objects representing payment indices
     * @throws ParseException if the prefix is missing, contains empty tokens, or invalid numbers
     */
    private List<Index> parsePaymentIndexes(ArgumentMultimap mm) throws ParseException {
        String paymentsRaw = mm.getValue(CliSyntax.PREFIX_PAYMENT_INDEX).orElse("").trim();
        if (paymentsRaw.isEmpty()) {
            throw new ParseException(MESSAGE_MISSING_PAY_INDEX);
        }

        String[] tokens = paymentsRaw.split(",", -1);
        List<Index> paymentIndexes = new ArrayList<>(tokens.length);

        for (String token : tokens) {
            String t = token.trim();
            if (t.isEmpty()) {
                throw new ParseException(MESSAGE_EMPTY_PAY_TOKENS);
            }
            // Let ParserUtil enforce numeric, positive, etc.
            paymentIndexes.add(ParserUtil.parseIndex(t));
        }

        if (paymentIndexes.isEmpty()) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, DeletePaymentCommand.MESSAGE_USAGE));
        }
        return paymentIndexes;
    }

    /**
     * Ensures that all payment indices in the list are unique.
     *
     * @param paymentIndexes the list of payment indices to validate
     * @throws ParseException if duplicate indices are detected
     */
    private void ensureNoDuplicatePaymentIndexes(List<Index> paymentIndexes) throws ParseException {
        Set<Integer> seenOneBased = new HashSet<>();
        for (Index idx : paymentIndexes) {
            int oneBased = idx.getOneBased();
            if (!seenOneBased.add(oneBased)) {
                throw new ParseException(MESSAGE_DUPLICATE_PAYMENT_INDEX);
            }
        }
    }
}
