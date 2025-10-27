package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ListArchivedCommand;

public class ListArchivedCommandParserTest {

    private final ListArchivedCommandParser parser = new ListArchivedCommandParser();

    @Test
    public void parse_empty_success() {
        assertParseSuccess(parser, "", new ListArchivedCommand());
    }

    @Test
    public void parse_whitespace_success() {
        assertParseSuccess(parser, "   \t \n ", new ListArchivedCommand());
    }

    @Test
    public void parseExtraArgs_throwsParseException() {
        assertParseFailure(
                parser,
                " anything",
                String.format(seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                        ListArchivedCommand.MESSAGE_USAGE)
        );
        assertParseFailure(
                parser,
                " 123",
                String.format(seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                        ListArchivedCommand.MESSAGE_USAGE)
        );
        assertParseFailure(
                parser,
                " archived please",
                String.format(seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                        ListArchivedCommand.MESSAGE_USAGE)
        );
    }

}

