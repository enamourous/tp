package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.TagContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object.
 * Now searches by tag instead of name.
 */
public class FindCommandParser implements Parser<FindCommand> {

    @Override
    public FindCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        List<String> keywords = Arrays.asList(trimmedArgs.split("\\s+"));

        Predicate<Person> combinedPredicate = person ->
                new NameContainsKeywordsPredicate(keywords).test(person)
                        || new TagContainsKeywordsPredicate(keywords).test(person);

        return new FindCommand(combinedPredicate);
    }
}
