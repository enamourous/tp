package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.RedoCommand;

public class RedoCommandParserTest {

    private final RedoCommandParser parser = new RedoCommandParser();

    @Test
    public void parseEmpty_success() {
        assertParseSuccess(parser, "", new RedoCommand());
    }

    @Test
    public void parseWhitespace_success() {
        assertParseSuccess(parser, "   \t \n ", new RedoCommand());
    }

    @Test
    public void parseExtraArgs_throwsParseException() {
        assertParseFailure(
                parser,
                " please",
                String.format(seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                        RedoCommand.MESSAGE_USAGE)
        );
        assertParseFailure(
                parser,
                " 123",
                String.format(seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                        RedoCommand.MESSAGE_USAGE)
        );
        assertParseFailure(
                parser,
                " again",
                String.format(seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                        RedoCommand.MESSAGE_USAGE)
        );
    }

}
