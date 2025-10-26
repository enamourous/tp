package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.UnarchiveCommand;

/**
 * Tests for UnarchiveCommandParser with 100% coverage.
 */
public class UnarchiveCommandParserTest {

    private final UnarchiveCommandParser parser = new UnarchiveCommandParser();

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertParseFailure(parser, "",
                String.format(seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                        UnarchiveCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "   ",
                String.format(seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                        UnarchiveCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_onlyCommas_throwsParseException() {
        assertParseFailure(parser, ",,,",
                String.format(seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                        UnarchiveCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " , , ",
                String.format(seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                        UnarchiveCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_nonNumericToken_throwsParseException() {
        assertParseFailure(parser, "one",
                String.format(seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                        UnarchiveCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "1, x",
                String.format(seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                        UnarchiveCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_zeroOrNegativeIndex_throwsParseException() {
        assertParseFailure(parser, "0",
                String.format(seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                        UnarchiveCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "-1",
                String.format(seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                        UnarchiveCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "1, -2",
                String.format(seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                        UnarchiveCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_singleIndex_success() {
        UnarchiveCommand expected = new UnarchiveCommand(Arrays.asList(INDEX_FIRST_PERSON));
        assertParseSuccess(parser, "1", expected);
        assertParseSuccess(parser, "  1  ", expected);
    }

    @Test
    public void parseMultipleIndicesWithSpaces_success() {
        UnarchiveCommand expected = new UnarchiveCommand(Arrays.asList(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON));
        assertParseSuccess(parser, "1,2", expected);
        assertParseSuccess(parser, " 1 , 2 ", expected);
        assertParseSuccess(parser, " 1 ,   2  ,   ", expected);
    }

    @Test
    public void parseDuplicatesInput_success_dedupByCommand() {
        UnarchiveCommand expected = new UnarchiveCommand(Arrays.asList(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON));
        assertParseSuccess(parser, "1,1,2,1", expected);
    }
}
