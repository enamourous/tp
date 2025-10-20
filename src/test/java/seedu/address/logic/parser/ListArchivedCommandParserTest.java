package seedu.address.logic.parser;

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
    public void parse_extraArgs_stillSuccess() {
        // Parser is lenient: any trailing args are ignored
        assertParseSuccess(parser, " anything", new ListArchivedCommand());
        assertParseSuccess(parser, " 123", new ListArchivedCommand());
        assertParseSuccess(parser, " archived please", new ListArchivedCommand());
    }
}

