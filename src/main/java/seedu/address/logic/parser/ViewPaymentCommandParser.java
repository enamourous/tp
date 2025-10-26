package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ViewPaymentCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code ViewPaymentsCommand} object.
 */
public class ViewPaymentCommandParser implements Parser<ViewPaymentCommand> {
    @Override
    public ViewPaymentCommand parse(String args) throws ParseException {
        String s = args.trim();
        if (s.equalsIgnoreCase("all")) {
            return new ViewPaymentCommand(null);
        }
        try {
            Index index = ParserUtil.parseIndex(s);
            return new ViewPaymentCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewPaymentCommand.MESSAGE_USAGE), pe);
        }
    }
}
