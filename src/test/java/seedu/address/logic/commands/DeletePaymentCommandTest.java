package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.payment.Amount;
import seedu.address.model.payment.Payment;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Unit tests for DeletePaymentCommand.
 */
public class DeletePaymentCommandTest {

    @Test
    public void execute_validIndex_success() throws Exception {
        Person alice = new PersonBuilder().withName("Alice").build();
        Payment payment = new Payment(new Amount(new BigDecimal("50.00")), LocalDate.of(2025, 1, 1));
        alice = alice.withAddedPayment(payment);

        Model model = new ModelStubWithPerson(alice);
        DeletePaymentCommand command = new DeletePaymentCommand(
                List.of(Index.fromOneBased(1)), Index.fromOneBased(1));

        CommandResult result = command.execute(model);

        assertEquals(String.format(DeletePaymentCommand.MESSAGE_SUCCESS, 1, "Alice"),
                result.getFeedbackToUser());
    }

    @Test
    public void execute_invalidPaymentIndex_throwsCommandException() {
        Person bob = new PersonBuilder().withName("Bob").build(); // no payments
        Model model = new ModelStubWithPerson(bob);

        DeletePaymentCommand command = new DeletePaymentCommand(
                List.of(Index.fromOneBased(1)), Index.fromOneBased(1));

        assertThrows(CommandException.class, () -> command.execute(model));
    }

    @Test
    public void execute_multiplePersons_success() throws Exception {
        Payment payment = new Payment(new Amount(new BigDecimal("20.00")), LocalDate.of(2025, 1, 1));

        Person person1 = new PersonBuilder().withName("Charlie").build();
        person1 = person1.withAddedPayment(payment);
        Person person2 = new PersonBuilder().withName("Dana").build();
        person2 = person2.withAddedPayment(payment);

        Model model = new ModelStubWithMultiplePersons(List.of(person1, person2));
        DeletePaymentCommand command = new DeletePaymentCommand(
                List.of(Index.fromOneBased(1), Index.fromOneBased(2)), Index.fromOneBased(1));

        CommandResult result = command.execute(model);

        assertEquals(String.format(DeletePaymentCommand.MESSAGE_SUCCESS, 1, "Charlie, Dana"),
                result.getFeedbackToUser());
    }

    @Test
    public void equals() {
        DeletePaymentCommand command1 = new DeletePaymentCommand(
                List.of(Index.fromOneBased(1)), Index.fromOneBased(1));
        DeletePaymentCommand command2 = new DeletePaymentCommand(
                List.of(Index.fromOneBased(1)), Index.fromOneBased(1));
        DeletePaymentCommand command3 = new DeletePaymentCommand(
                List.of(Index.fromOneBased(2)), Index.fromOneBased(1));

        // same values -> returns true
        assertEquals(command1, command2);

        // different values -> not equal
        assertThrows(AssertionError.class, () -> assertEquals(command1, command3));
    }

    // ===== Model Stubs =====

    private static class ModelStub implements Model {

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(java.util.function.Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setUserPrefs(seedu.address.model.ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public seedu.address.model.ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(seedu.address.commons.core.GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public seedu.address.commons.core.GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(java.nio.file.Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public java.nio.file.Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(seedu.address.model.ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public seedu.address.model.ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

    }

    private static class ModelStubWithPerson extends ModelStub {
        private Person person;

        ModelStubWithPerson(Person person) {
            this.person = person;
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return FXCollections.observableArrayList(person);
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            this.person = editedPerson;
        }
    }

    private static class ModelStubWithMultiplePersons extends ModelStub {
        private final List<Person> persons;

        ModelStubWithMultiplePersons(List<Person> persons) {
            this.persons = new ArrayList<>(persons);
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return FXCollections.observableArrayList(persons);
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            int index = persons.indexOf(target);
            persons.set(index, editedPerson);
        }
    }
}
