package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.AddPaymentCommandParser.MESSAGE_INVALID_AMOUNT;
import static seedu.address.logic.parser.AddPaymentCommandParser.MESSAGE_INVALID_DATE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddPaymentCommand;
import seedu.address.model.payment.Amount;

/**
 * Tests for AddPaymentCommandParser.
 *
 * - empty / missing args
 * - missing amount or date prefix
 * - invalid index tokens
 * - invalid amount formats
 * - invalid date formats
 * - valid single index
 * - valid multiple indexes
 * - trailing commas/whitespace handling
 * - multiple indexes, remarks optional
 */

public class AddPaymentCommandParserTest {

    private static final String MESSAGE_USAGE =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPaymentCommand.MESSAGE_USAGE);

    private final AddPaymentCommandParser parser = new AddPaymentCommandParser();

    // 1. Empty or missing args
    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertParseFailure(parser, "", MESSAGE_USAGE);
        assertParseFailure(parser, "   ", MESSAGE_USAGE);
    }

    // 2. Missing required prefixes
    @Test
    public void parse_missingPrefixes_throwsParseException() {
        // Missing a/ prefix
        assertParseFailure(parser, "1 d/2025-10-09", MESSAGE_USAGE);

        // Missing d/ prefix
        assertParseFailure(parser, "1 a/23.50", MESSAGE_USAGE);

        // Missing both prefixes
        assertParseFailure(parser, "1", MESSAGE_USAGE);
    }

    // 3. Invalid indexes
    @Test
    public void parse_invalidIndex_throwsParseException() {
        // Non-numeric
        assertParseFailure(parser, "a a/23.50 d/2025-10-09", MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, "1, x a/23.50 d/2025-10-09", MESSAGE_INVALID_INDEX);

        // Zero or negative
        assertParseFailure(parser, "0 a/23.50 d/2025-10-09", MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, "-1 a/23.50 d/2025-10-09", MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, "1, -2 a/23.50 d/2025-10-09", MESSAGE_INVALID_INDEX);
    }

    // 4. Invalid amount formats
    @Test
    public void parse_invalidAmount_throwsParseException() {
        // Negative
        assertParseFailure(parser, "1 a/-5.00 d/2025-10-09", MESSAGE_INVALID_AMOUNT);

        // Too many decimal places
        assertParseFailure(parser, "1 a/23.999 d/2025-10-09", MESSAGE_INVALID_AMOUNT);

        // Non-numeric
        assertParseFailure(parser, "1 a/abc d/2025-10-09", MESSAGE_INVALID_AMOUNT);
    }

    // 5. Invalid date formats
    @Test
    public void parse_invalidDate_throwsParseException() {
        // Invalid month
        assertParseFailure(parser, "1 a/23.50 d/2025-13-09", MESSAGE_INVALID_DATE);

        // Invalid day
        assertParseFailure(parser, "1 a/23.50 d/2025-10-32", MESSAGE_INVALID_DATE);

        // Non-numeric date
        assertParseFailure(parser, "1 a/23.50 d/today", MESSAGE_INVALID_DATE);
    }

    // 6. Single index, valid input
    @Test
    public void parse_singleIndexValidArgs_success() {
        AddPaymentCommand expected = new AddPaymentCommand(
                List.of(INDEX_FIRST_PERSON),
                Amount.parse("23.50"),
                LocalDate.of(2025, 10, 9),
                null);

        assertParseSuccess(parser, "1 a/23.50 d/2025-10-09", expected);
        assertParseSuccess(parser, "  1  a/23.50 d/2025-10-09  ", expected);
    }

    // 7. Single index with remarks
    @Test
    public void parse_singleIndexWithRemarks_success() {
        AddPaymentCommand expected = new AddPaymentCommand(
                List.of(INDEX_FIRST_PERSON),
                Amount.parse("45.00"),
                LocalDate.of(2025, 11, 10),
                "taxi home");

        assertParseSuccess(parser, "1 a/45.00 d/2025-11-10 r/taxi home", expected);
        assertParseSuccess(parser, "1 a/45.00 d/2025-11-10 r/ taxi home ", expected);
    }

    // 8. Multiple indexes
    @Test
    public void parse_multipleIndexes_success() {
        AddPaymentCommand expected = new AddPaymentCommand(
                Arrays.asList(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON),
                Amount.parse("12.34"),
                LocalDate.of(2025, 12, 1),
                null);

        assertParseSuccess(parser, "1,2 a/12.34 d/2025-12-01", expected);
        assertParseSuccess(parser, " 1 , 2  a/12.34 d/2025-12-01", expected);
    }

    // 9. Multiple indexes with remarks and trailing comma
    @Test
    public void parse_multipleIndexesWithRemarksAndTrailingComma_success() {
        AddPaymentCommand expected = new AddPaymentCommand(
                Arrays.asList(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON),
                Amount.parse("50.00"),
                LocalDate.of(2025, 10, 20),
                "team lunch");

        // Note: trailing comma ignored
        assertParseSuccess(parser, "1,2, a/50.00 d/2025-10-20 r/team lunch", expected);
        assertParseSuccess(parser, " 1 , 2 ,  a/50.00 d/2025-10-20 r/ team lunch ", expected);
    }

    // 10. Missing preamble (indexes)
    @Test
    public void parse_missingPreamble_throwsParseException() {
        assertParseFailure(parser, "a/23.50 d/2025-10-09", MESSAGE_USAGE);
    }

}
