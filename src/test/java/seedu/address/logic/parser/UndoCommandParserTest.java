package seedu.address.logic.parser;

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
    public void parseExtraArgs_stillSuccess() {
        // Parser is lenient: any trailing args are ignored
        assertParseSuccess(parser, " please", new UndoCommand());
        assertParseSuccess(parser, " 123", new UndoCommand());
        assertParseSuccess(parser, " now", new UndoCommand());
    }
}

