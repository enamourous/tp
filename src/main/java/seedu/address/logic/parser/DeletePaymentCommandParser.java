package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeletePaymentCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeletePaymentCommand object.
 */
public class DeletePaymentCommandParser implements Parser<DeletePaymentCommand> {

    @Override
    public DeletePaymentCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, CliSyntax.PREFIX_PAYMENT_INDEX);

        List<Index> personIndexes;
        Index paymentIndex;

        try {
            // Parse person indexes from preamble
            personIndexes = ParserUtil.parseIndexes(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeletePaymentCommand.MESSAGE_USAGE));
        }

        try {
            // Parse payment index from prefix
            paymentIndex = ParserUtil.parseIndex(
                    argMultimap.getValue(CliSyntax.PREFIX_PAYMENT_INDEX)
                            .orElseThrow(() ->
                                    new ParseException(
                                            String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                                                    DeletePaymentCommand.MESSAGE_USAGE)))
            );
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeletePaymentCommand.MESSAGE_USAGE));
        }

        return new DeletePaymentCommand(personIndexes, paymentIndex);
    }
}
