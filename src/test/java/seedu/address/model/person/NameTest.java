package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class NameTest {

    // ---------------------------
    // Constructor behavior
    // ---------------------------

    @Test
    @DisplayName("constructor(null) -> NullPointerException")
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Name(null));
    }

    @Test
    @DisplayName("constructor(invalid) -> IllegalArgumentException")
    public void constructor_invalidName_throwsIllegalArgumentException() {
        // A representative set; full invalid set is covered below in isValidName()
        assertThrows(IllegalArgumentException.class, () -> new Name(""));
        assertThrows(IllegalArgumentException.class, () -> new Name(" "));
        assertThrows(IllegalArgumentException.class, () -> new Name("John2"));
        assertThrows(IllegalArgumentException.class, () -> new Name(" John"));
        assertThrows(IllegalArgumentException.class, () -> new Name("John@"));
    }

    // ---------------------------
    // Validation behavior
    // ---------------------------

    @Test
    @DisplayName("isValidName(null) -> NullPointerException")
    public void isValidName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> Name.isValidName(null));
    }

    @Test
    @DisplayName("isValidName(invalid cases)")
    public void isValidName_invalid() {
        // Empty / whitespace-only
        assertFalse(Name.isValidName("")); // empty string
        assertFalse(Name.isValidName(" ")); // one space
        assertFalse(Name.isValidName("   ")); // multiple spaces

        // Must start with a letter
        assertFalse(Name.isValidName(" John")); // leading space
        assertFalse(Name.isValidName("-John")); // leading hyphen
        assertFalse(Name.isValidName("'John")); // leading apostrophe
        assertFalse(Name.isValidName(".John")); // leading period
        assertFalse(Name.isValidName("2John")); // leading digit

        // Disallow digits anywhere
        assertFalse(Name.isValidName("John2"));
        assertFalse(Name.isValidName("peter the 2nd"));
        assertFalse(Name.isValidName("12345"));

        // Disallow other symbols
        assertFalse(Name.isValidName("^"));
        assertFalse(Name.isValidName("peter*"));
        assertFalse(Name.isValidName("John@Doe"));
        assertFalse(Name.isValidName("John_Doe")); // underscore not allowed

        // Edge: only punctuation (no letters)
        assertFalse(Name.isValidName(".'-")); // has allowed punctuation but no letters
        assertFalse(Name.isValidName(".")); // single period, no letters
        assertFalse(Name.isValidName("-")); // single hyphen, no letters
        assertFalse(Name.isValidName("'")); // single apostrophe, no letters
    }

    @Test
    @DisplayName("isValidName(valid cases)")
    public void isValidName_valid() {
        // Simple alphabetic
        assertTrue(Name.isValidName("peter jack"));
        assertTrue(Name.isValidName("Capital Tan"));
        assertTrue(Name.isValidName("A")); // single-letter name

        // Common punctuation in names
        assertTrue(Name.isValidName("Jean-Luc Picard")); // hyphen
        assertTrue(Name.isValidName("Anne-Marie")); // hyphen internal
        assertTrue(Name.isValidName("O'Connor")); // apostrophe (ASCII)
        assertTrue(Name.isValidName("J. K. Rowling")); // periods in initials
        assertTrue(Name.isValidName("Mary Jane")); // single space between words

        // Unicode letters (international names)
        assertTrue(Name.isValidName("José María")); // accented letters

        // Boundary around starting-with-letter rule
        assertTrue(Name.isValidName("A.")); // letter + period
        assertTrue(Name.isValidName("J.")); // initial style
    }

    // ---------------------------
    // equals / hashCode / toString
    // ---------------------------

    @Test
    @DisplayName("equals: reflexive, symmetric, transitive, and null/type safety")
    public void equals_contract() {
        Name a1 = new Name("Valid Name");
        Name a2 = new Name("Valid Name");
        Name b = new Name("Other Valid Name");

        // Reflexive
        assertTrue(a1.equals(a1));

        // Symmetric
        assertTrue(a1.equals(a2));
        assertTrue(a2.equals(a1));

        // Transitive
        Name a3 = new Name("Valid Name");
        assertTrue(a1.equals(a2));
        assertTrue(a2.equals(a3));
        assertTrue(a1.equals(a3));

        // Consistent & inequality
        assertFalse(a1.equals(b));
        assertFalse(b.equals(a1));

        // Null & type safety
        assertFalse(a1.equals(null));
        assertFalse(a1.equals(5.0f));
    }

    @Test
    @DisplayName("hashCode: equal objects have equal hash codes")
    public void hashCode_consistency() {
        Name a1 = new Name("Valid Name");
        Name a2 = new Name("Valid Name");
        Name b = new Name("Other Valid Name");

        assertEquals(a1.hashCode(), a2.hashCode());
        assertNotEquals(a1.hashCode(), b.hashCode());
    }

    @Test
    @DisplayName("toString returns the full name verbatim")
    public void toString_returnsFullName() {
        assertEquals("Alex Yeoh", new Name("Alex Yeoh").toString());
        assertEquals("J. K. Rowling", new Name("J. K. Rowling").toString());
    }
}
