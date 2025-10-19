package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_MATRICULATIONNUM_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_MATRICULATIONNUM_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.testutil.PersonBuilder;

public class UniquePersonListTest {

    private final UniquePersonList uniquePersonList = new UniquePersonList();

    @Test
    public void contains_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniquePersonList.contains(null));
    }

    @Test
    public void contains_personNotInList_returnsFalse() {
        assertFalse(uniquePersonList.contains(ALICE));
    }

    @Test
    public void contains_personInList_returnsTrue() {
        uniquePersonList.add(ALICE);
        assertTrue(uniquePersonList.contains(ALICE));
    }

    @Test
    public void contains_personWithSameMatricNumberInList_returnsTrue() {
        uniquePersonList.add(ALICE);
        // editedAlice has different name/tags but same matric number
        Person editedAlice = new PersonBuilder(ALICE)
            .withTags(VALID_TAG_HUSBAND)
            .withName(VALID_NAME_BOB)
            .build();
        assertTrue(uniquePersonList.contains(editedAlice));
    }

    @Test
    public void contains_personWithDifferentMatricNumber_returnsFalse() {
        uniquePersonList.add(ALICE);
        // editedAlice has a different matric number → should not be same person
        Person editedAlice = new PersonBuilder(ALICE)
            .withMatriculationNumber(VALID_MATRICULATIONNUM_BOB)
            .build();
        assertFalse(uniquePersonList.contains(editedAlice));
    }

    @Test
    public void add_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniquePersonList.add(null));
    }

    @Test
    public void add_duplicatePerson_throwsDuplicatePersonException() {
        uniquePersonList.add(ALICE);
        assertThrows(DuplicatePersonException.class, () -> uniquePersonList.add(ALICE));
    }

    @Test
    public void setPerson_nullTargetPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniquePersonList.setPerson(null, ALICE));
    }

    @Test
    public void setPerson_nullEditedPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniquePersonList.setPerson(ALICE, null));
    }

    @Test
    public void setPerson_targetPersonNotInList_throwsPersonNotFoundException() {
        assertThrows(PersonNotFoundException.class, () -> uniquePersonList.setPerson(ALICE, ALICE));
    }

    @Test
    public void setPerson_editedPersonIsSamePerson_success() {
        uniquePersonList.add(ALICE);
        uniquePersonList.setPerson(ALICE, ALICE);
        UniquePersonList expectedUniquePersonList = new UniquePersonList();
        expectedUniquePersonList.add(ALICE);
        assertEquals(expectedUniquePersonList, uniquePersonList);
    }

    @Test
    public void setPerson_editedPersonHasSameMatricNumber_success() {
        uniquePersonList.add(ALICE);
        // Same matric number but different name and tags
        Person editedAlice = new PersonBuilder(ALICE)
            .withName("Different Name")
            .withTags(VALID_TAG_HUSBAND)
            .withMatriculationNumber(VALID_MATRICULATIONNUM_AMY)
            .build();
        uniquePersonList.setPerson(ALICE, editedAlice);
        UniquePersonList expectedUniquePersonList = new UniquePersonList();
        expectedUniquePersonList.add(editedAlice);
        assertEquals(expectedUniquePersonList, uniquePersonList);
    }

    @Test
    public void setPerson_editedPersonHasDifferentMatricNumber_success() {
        uniquePersonList.add(ALICE);
        // different matric number → different identity
        Person editedAlice = new PersonBuilder(BOB)
            .withMatriculationNumber(VALID_MATRICULATIONNUM_BOB)
            .build();
        uniquePersonList.setPerson(ALICE, editedAlice);
        UniquePersonList expectedUniquePersonList = new UniquePersonList();
        expectedUniquePersonList.add(editedAlice);
        assertEquals(expectedUniquePersonList, uniquePersonList);
    }

    @Test
    public void setPerson_editedPersonHasNonUniqueMatricNumber_throwsDuplicatePersonException() {
        uniquePersonList.add(ALICE);
        uniquePersonList.add(BOB);
        // Edited Alice now shares BOB’s matric number → duplicate
        Person editedAlice = new PersonBuilder(ALICE)
            .withMatriculationNumber(BOB.getMatriculationNumber().value)
            .build();
        assertThrows(DuplicatePersonException.class, () -> uniquePersonList.setPerson(ALICE, editedAlice));
    }

    @Test
    public void remove_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniquePersonList.remove(null));
    }

    @Test
    public void remove_personDoesNotExist_throwsPersonNotFoundException() {
        assertThrows(PersonNotFoundException.class, () -> uniquePersonList.remove(ALICE));
    }

    @Test
    public void remove_existingPerson_removesPerson() {
        uniquePersonList.add(ALICE);
        uniquePersonList.remove(ALICE);
        UniquePersonList expectedUniquePersonList = new UniquePersonList();
        assertEquals(expectedUniquePersonList, uniquePersonList);
    }

    @Test
    public void setPersons_nullUniquePersonList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniquePersonList.setPersons((UniquePersonList) null));
    }

    @Test
    public void setPersons_uniquePersonList_replacesOwnListWithProvidedUniquePersonList() {
        uniquePersonList.add(ALICE);
        UniquePersonList expectedUniquePersonList = new UniquePersonList();
        expectedUniquePersonList.add(BOB);
        uniquePersonList.setPersons(expectedUniquePersonList);
        assertEquals(expectedUniquePersonList, uniquePersonList);
    }

    @Test
    public void setPersons_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniquePersonList.setPersons((List<Person>) null));
    }

    @Test
    public void setPersons_list_replacesOwnListWithProvidedList() {
        uniquePersonList.add(ALICE);
        List<Person> personList = Collections.singletonList(BOB);
        uniquePersonList.setPersons(personList);
        UniquePersonList expectedUniquePersonList = new UniquePersonList();
        expectedUniquePersonList.add(BOB);
        assertEquals(expectedUniquePersonList, uniquePersonList);
    }

    @Test
    public void setPersons_listWithDuplicateMatricNumbers_throwsDuplicatePersonException() {
        Person aliceDuplicate = new PersonBuilder(ALICE)
            .withMatriculationNumber(ALICE.getMatriculationNumber().value)
            .build();
        List<Person> listWithDuplicatePersons = Arrays.asList(ALICE, aliceDuplicate);
        assertThrows(DuplicatePersonException.class, () -> uniquePersonList.setPersons(listWithDuplicatePersons));
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, (
        ) -> uniquePersonList.asUnmodifiableObservableList().remove(0));
    }

    @Test
    public void toStringMethod() {
        assertEquals(uniquePersonList.asUnmodifiableObservableList().toString(), uniquePersonList.toString());
    }
}
