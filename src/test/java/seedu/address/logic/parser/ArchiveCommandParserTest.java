package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ArchiveCommand;

/**
 * Tests for ArchiveCommandParser.
 * Targets 100% line/branch coverage:
 *  - empty args (trimmed empty) -> failure (hits catch)
 *  - only commas/whitespace -> failure (indexes.isEmpty path, hits catch)
 *  - non-numeric token -> failure (parseIndex throws, hits catch)
 *  - zero/negative index -> failure (parseIndex throws, hits catch)
 *  - single index success
 *  - multiple indices with spaces success
 *  - duplicates are allowed in input and de-duplicated by ArchiveCommand ctor
 */
public class ArchiveCommandParserTest {

    private final ArchiveCommandParser parser = new ArchiveCommandParser();

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertParseFailure(parser, "",
                String.format(seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                        ArchiveCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "   ",
                String.format(seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                        ArchiveCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_onlyCommas_throwsParseException() {
        assertParseFailure(parser, ",,,",
                String.format(seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                        ArchiveCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " , , ",
                String.format(seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                        ArchiveCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_nonNumericToken_throwsParseException() {
        assertParseFailure(parser, "one",
                String.format(seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                        ArchiveCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "1, x",
                String.format(seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                        ArchiveCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_zeroOrNegativeIndex_throwsParseException() {
        assertParseFailure(parser, "0",
                String.format(seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                        ArchiveCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "-1",
                String.format(seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                        ArchiveCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "1, -2",
                String.format(seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                        ArchiveCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_singleIndex_success() {
        ArchiveCommand expected = new ArchiveCommand(Arrays.asList(INDEX_FIRST_PERSON));
        assertParseSuccess(parser, "1", expected);
        assertParseSuccess(parser, "  1  ", expected);
    }

    @Test
    public void parseMultipleIndicesWithSpaces_success() {
        ArchiveCommand expected = new ArchiveCommand(Arrays.asList(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON));
        assertParseSuccess(parser, "1,2", expected);
        assertParseSuccess(parser, " 1 , 2 ", expected);
        assertParseSuccess(parser, " 1 ,   2  ,   ", expected); // trailing comma/whitespace ignored
    }

    @Test
    public void parseDuplicatesInput_success_dedupByCommand() {
        // Parser accepts duplicates; ArchiveCommand's constructor removes duplicates while preserving order.
        ArchiveCommand expected = new ArchiveCommand(Arrays.asList(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON));
        assertParseSuccess(parser, "1,1,2,1", expected);
    }
}
