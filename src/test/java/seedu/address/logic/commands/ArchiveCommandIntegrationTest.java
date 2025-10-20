package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.logic.Logic;
import seedu.address.logic.LogicManager;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;

public class ArchiveCommandIntegrationTest {

    @TempDir
    public Path tempDir;

    private Model model;
    private ReadOnlyAddressBook originalBook; // snapshot of typical book for name lookups
    private Logic logic;

    @BeforeEach
    public void setUp() {
        // Model with typical persons
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        originalBook = new AddressBook(model.getAddressBook());

        JsonAddressBookStorage addressBookStorage =
                new JsonAddressBookStorage(tempDir.resolve("addressBook.json"));
        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(tempDir.resolve("userPrefs.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage);

        logic = new LogicManager(model, storage);
    }

    @Test
    public void archive_integration() throws Exception {
        // Ensure we're in active view to begin with
        logic.execute("list");

        // Grab the names of the first two persons BEFORE archiving
        Person first = logic.getFilteredPersonList().get(0);
        Person second = logic.getFilteredPersonList().get(1);
        String firstName = first.getName().toString();
        String secondName = second.getName().toString();

        // 1) archive two people
        String archiveCmd = "archive 1,2";
        String archiveMsg = logic.execute(archiveCmd).getFeedbackToUser();
        // Message: "Archived: NAME1, NAME2"
        String expectedArchiveMsg = String.format(ArchiveCommand.MESSAGE_SUCCESS, firstName + ", " + secondName);
        assertEquals(expectedArchiveMsg, archiveMsg);

        // After archiving, the active list should not include those persons
        List<String> activeNames = logic.getFilteredPersonList().stream()
                .map(p -> p.getName().toString()).collect(Collectors.toList());
        assertTrue(!activeNames.contains(firstName) && !activeNames.contains(secondName));

        // 2) listarchived should show exactly those two and both must be archived
        String listArchivedMsg = logic.execute("listarchived").getFeedbackToUser();
        assertEquals(ListArchivedCommand.MESSAGE_SUCCESS, listArchivedMsg);

        List<Person> archivedView = logic.getFilteredPersonList();
        assertEquals(2, archivedView.size());
        assertTrue(archivedView.stream().allMatch(Person::isArchived));

        List<String> archivedNames = archivedView.stream()
                .map(p -> p.getName().toString()).collect(Collectors.toList());
        assertTrue(archivedNames.contains(firstName) && archivedNames.contains(secondName));

        // 3) unarchive the first one in the archived view (index 1 refers to first archived)
        String unarchiveMsg = logic.execute("unarchive 1").getFeedbackToUser();
        String expectedUnarchiveMsg = String.format(UnarchiveCommand.MESSAGE_SUCCESS, firstName);
        assertEquals(expectedUnarchiveMsg, unarchiveMsg);

        // Re-open archived view and check only the second remains archived.
        logic.execute("listarchived");
        List<Person> remainderArchived = logic.getFilteredPersonList();
        assertEquals(1, remainderArchived.size());
        assertEquals(secondName, remainderArchived.get(0).getName().toString());
        assertTrue(remainderArchived.get(0).isArchived());

        // And the unarchived first should now appear in active list
        logic.execute("list"); // ensure active view
        List<String> activeNamesAfter = logic.getFilteredPersonList().stream()
                .map(p -> p.getName().toString()).collect(Collectors.toList());
        assertTrue(activeNamesAfter.contains(firstName));
    }
}
