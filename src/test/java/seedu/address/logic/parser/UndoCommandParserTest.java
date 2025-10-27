package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.UndoCommand;

public class UndoCommandParserTest {

    private final UndoCommandParser parser = new UndoCommandParser();

    @Test
    public void parseEmpty_success() {
        assertParseSuccess(parser, "", new UndoCommand());
    }

    @Test
    public void parseWhitespace_success() {
        assertParseSuccess(parser, "   \t \n ", new UndoCommand());
    }

    @Test
    public void parseExtraArgs_throwsParseException() {
        assertParseFailure(
                parser,
                " please",
                String.format(seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                        UndoCommand.MESSAGE_USAGE)
        );
        assertParseFailure(
                parser,
                " 123",
                String.format(seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                        UndoCommand.MESSAGE_USAGE)
        );
        assertParseFailure(
                parser,
                " now",
                String.format(seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                        UndoCommand.MESSAGE_USAGE)
        );
    }

}

