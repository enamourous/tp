package seedu.address.logic.parser;

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
    public void parseExtraArgs_stillSuccess() {
        // Parser is lenient: any trailing args are ignored
        assertParseSuccess(parser, " please", new RedoCommand());
        assertParseSuccess(parser, " 123", new RedoCommand());
        assertParseSuccess(parser, " again", new RedoCommand());
    }
}
