package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.exceptions.InvalidMatriculationNumberException;

/**
 * Unit tests for {@link MatriculationNumber}.
 */
public class MatriculationNumberTest {

    // ✅ ---------------- VALID CASES ----------------
    @Test
    public void constructor_validMatriculationNumber_success() {
        MatriculationNumber m = new MatriculationNumber("A01234567X");
        assertEquals("A01234567X", m.value);
    }

    @Test
    public void constructor_lowercase_convertedToUppercase() {
        MatriculationNumber m = new MatriculationNumber("a01234567b");
        assertEquals("A01234567B", m.value);
    }

    @Test
    public void isValidMatriculationNumber_validExamples_returnTrue() {
        assertTrue(MatriculationNumber.isValidMatriculationNumber("A01234567X"));
        assertTrue(MatriculationNumber.isValidMatriculationNumber("A17172828B"));
        assertTrue(MatriculationNumber.isValidMatriculationNumber("A00000000Z"));
    }

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new MatriculationNumber(null));
    }

    @Test
    public void constructor_invalidMatriculationNumber_throwsIllegalArgumentException() {
        // too short
        assertThrows(InvalidMatriculationNumberException.class, () -> new MatriculationNumber("A123B"));
        // too long (11 chars)
        assertThrows(InvalidMatriculationNumberException.class, () -> new MatriculationNumber("A123456789"));
        // double letters at end (11 chars)
        assertThrows(InvalidMatriculationNumberException.class, () -> new MatriculationNumber("A1234567BB"));
        // lowercase ending
        assertThrows(InvalidMatriculationNumberException.class, () -> new MatriculationNumber("A12345678bb"));
        // starts with lowercase letter
        assertThrows(InvalidMatriculationNumberException.class, () -> new MatriculationNumber("b12312311b"));
        // starts with wrong letter
        assertThrows(InvalidMatriculationNumberException.class, () -> new MatriculationNumber("C12312311B"));
        // starts with digit
        assertThrows(InvalidMatriculationNumberException.class, () -> new MatriculationNumber("12312312HH"));
        // no digits in middle
        assertThrows(InvalidMatriculationNumberException.class, () -> new MatriculationNumber("AABCDEFGHX"));
        // only 9 total characters
        assertThrows(InvalidMatriculationNumberException.class, () -> new MatriculationNumber("A1234567B"));
    }

    @Test
    public void isValidMatriculationNumber_invalidExamples_returnFalse() {
        String[] invalids = {
            "", " ", "A", "A123B", "A123456789", "A1234567BB",
            "a1234567b", "B01234567X", "C12312311B", "12312312HH",
            "AABCDEFGHX", "A1234567@", "A12", "A00000000", "Z00000000X"
        };
        for (String test : invalids) {
            assertFalse(MatriculationNumber.isValidMatriculationNumber(test), "Failed at: " + test);
        }
    }

    @Test
    public void equals_sameValue_true() {
        MatriculationNumber m1 = new MatriculationNumber("A01234567X");
        MatriculationNumber m2 = new MatriculationNumber("a01234567x");
        assertEquals(m1, m2);
    }

    @Test
    public void equals_differentValue_false() {
        MatriculationNumber m1 = new MatriculationNumber("A01234567X");
        MatriculationNumber m2 = new MatriculationNumber("A01234567Y");
        assertNotEquals(m1, m2);
    }

    @Test
    public void equals_sameObject_true() {
        MatriculationNumber m = new MatriculationNumber("A01234567X");
        assertTrue(m.equals(m)); // self equality
    }

    @Test
    public void equals_differentType_false() {
        MatriculationNumber m = new MatriculationNumber("A01234567X");
        assertFalse(m.equals("A01234567X")); // different type
        assertFalse(m.equals(123)); // integer
    }

    @Test
    public void hashCode_sameValue_sameHash() {
        MatriculationNumber m1 = new MatriculationNumber("A01234567X");
        MatriculationNumber m2 = new MatriculationNumber("a01234567x");
        assertEquals(m1.hashCode(), m2.hashCode());
    }

    @Test
    public void toString_returnsValue() {
        MatriculationNumber m = new MatriculationNumber("A01234567X");
        assertEquals("A01234567X", m.toString());
    }

}
