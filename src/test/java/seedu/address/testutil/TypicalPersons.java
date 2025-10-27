package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_MATRICULATIONNUM_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_MATRICULATIONNUM_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.person.Person;

/**
 * A utility class containing a list of {@code Person} objects to be used in tests.
 */
public class TypicalPersons {

    public static final Person ALICE = new PersonBuilder().withName("Alice Pauline")
        .withMatriculationNumber("A0000000D").withEmail("alice@example.com")
        .withPhone("13658964")
        .withTags("friends").build();

    public static final Person BENSON = new PersonBuilder().withName("Benson Meier")
        .withMatriculationNumber("A0000000E")
        .withEmail("johnd@example.com").withPhone("69834754")
        .withTags("owesMoney", "friends").build();

    public static final Person CARL = new PersonBuilder().withName("Carl Kurz").withPhone("99997765")
        .withEmail("heinz@example.com").withMatriculationNumber("A0000000F").build();

    public static final Person DANIEL = new PersonBuilder().withName("Daniel Meier").withPhone("27547468")
        .withEmail("cornelia@example.com").withMatriculationNumber("A0000000G").withTags("friends").build();

    public static final Person ELLE = new PersonBuilder().withName("Elle Meyer").withPhone("27527468")
        .withEmail("werner@example.com").withMatriculationNumber("A0000000H").build();

    public static final Person FIONA = new PersonBuilder().withName("Fiona Kunz").withPhone("27547468")
        .withEmail("lydia@example.com").withMatriculationNumber("A0000000I").build();

    public static final Person GEORGE = new PersonBuilder().withName("George Best").withPhone("27547468")
        .withEmail("anna@example.com").withMatriculationNumber("A0000000J").build();

    // Manually added
    public static final Person HOON = new PersonBuilder().withName("Hoon Meier").withPhone("23456789")
        .withEmail("stefan@example.com").withMatriculationNumber("A2232222H").build();

    public static final Person IDA = new PersonBuilder().withName("Ida Mueller").withPhone("12345677")
        .withEmail("hans@example.com").withMatriculationNumber("A2222322I").build();

    // Manually added - Person's details found in {@code CommandTestUtil}
    public static final Person AMY = new PersonBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
        .withEmail(VALID_EMAIL_AMY).withMatriculationNumber(
            VALID_MATRICULATIONNUM_AMY).withTags(VALID_TAG_FRIEND).build();

    public static final Person BOB = new PersonBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
        .withEmail(VALID_EMAIL_BOB).withMatriculationNumber(VALID_MATRICULATIONNUM_BOB).withTags(VALID_TAG_HUSBAND,
            VALID_TAG_FRIEND)
        .build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalPersons() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical persons.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Person person : getTypicalPersons()) {
            ab.addPerson(person);
        }
        return ab;
    }

    public static List<Person> getTypicalPersons() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
