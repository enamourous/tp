package seedu.address.model.person;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import seedu.address.model.tag.Tag;

/**
 * Tests that a {@code Person}'s {@code Tag} matches any of the keywords given.
 */

public class TagContainsKeywordsPredicate implements Predicate<Person> {
    private final List<String> keywords;

    public TagContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Person person) {
        Set<Tag> tags = person.getTags();
        for (Tag tag : tags) {
            String tagName = tag.tagName.toLowerCase();
            for (String keyword : keywords) {
                if (tagName.equalsIgnoreCase(keyword)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof TagContainsKeywordsPredicate
            && keywords.equals(((TagContainsKeywordsPredicate) other).keywords)); // state check
    }
}
