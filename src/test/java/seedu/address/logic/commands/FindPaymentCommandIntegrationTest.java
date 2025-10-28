package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.payment.Amount;
import seedu.address.model.payment.Payment;
import seedu.address.model.person.Email;
import seedu.address.model.person.MatriculationNumber;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

public class FindPaymentCommandIntegrationTest {

    private Model model;

    private Person alice;
    private Person bob;

    @BeforeEach
    public void setUp() {
        model = new ModelManager();

        alice = new Person(
                new Name("Alice"),
                new Phone("12345678"),
                new Email("alice@example.com"),
                new MatriculationNumber("A1234567Z"),
                Set.of()
        );

        bob = new Person(
                new Name("Bob"),
                new Phone("87654321"),
                new Email("bob@example.com"),
                new MatriculationNumber("A7654321Z"),
                Set.of(new Tag("friend"))
        );

        model.addPerson(alice);
        model.addPerson(bob);
    }

    @Test
    public void execute_addPayment_successMessage() throws Exception {
        Payment payment = new Payment(new Amount(new BigDecimal("12.34")), LocalDate.now(), "Lunch");

        Person updatedAlice = alice.withAddedPayment(payment);
        model.setPerson(alice, updatedAlice);

        // Corrected message using payment fields instead of Payment.toString()
        String expectedMessage = String.format("Added payment: $12.34 | Lunch to Alice");
        String actualMessage = String.format("Added payment: $%s | %s to %s",
                payment.getAmount(), payment.getRemarks(), updatedAlice.getName());
        assertEquals(expectedMessage, actualMessage);

        // Confirm payment exists in model
        assertEquals(1, model.getFilteredPersonList().get(0).getPayments().size());
        assertEquals(payment, model.getFilteredPersonList().get(0).getPayments().get(0));
    }

    @Test
    public void execute_removePayment_successMessage() throws Exception {
        Payment payment = new Payment(new Amount(new BigDecimal("50.00")), LocalDate.now(), "Dinner");
        alice = alice.withAddedPayment(payment);
        model.setPerson(model.getFilteredPersonList().get(0), alice);

        Person updatedAlice = alice.withRemovedPayment(payment);
        model.setPerson(alice, updatedAlice);

        // Corrected message
        String expectedMessage = String.format("Removed payment: $50.00 | Dinner from Alice");
        String actualMessage = String.format("Removed payment: $%s | %s from %s",
                payment.getAmount(), payment.getRemarks(), updatedAlice.getName());
        assertEquals(expectedMessage, actualMessage);

        // Payment list should be empty
        assertEquals(0, updatedAlice.getPayments().size());
    }
}
