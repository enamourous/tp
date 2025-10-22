package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.payment.Payment;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final MatriculationNumber matriculationNumber;
    private final Set<Tag> tags = new HashSet<>();
    private final List<Payment> payments;
    private final boolean archived;

    /**
     * Minimal constructor (AB3 default fields). Starts with no payments.
     */
    public Person(Name name, Phone phone, Email email, MatriculationNumber matriculationNumber,
                  Set<Tag> tags) {
        requireAllNonNull(name, phone, email, matriculationNumber, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.matriculationNumber = matriculationNumber;
        this.tags.addAll(tags);
        this.archived = false;
        this.payments = Collections.unmodifiableList(new ArrayList<>()); // empty immutable list
    }

    /**
     * Full constructor including payments.
     */
    public Person(Name name, Phone phone, Email email, MatriculationNumber matriculationNumber,
                  Set<Tag> tags, boolean archived, List<Payment> payments) {
        requireAllNonNull(name, phone, email, matriculationNumber, tags, payments);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.matriculationNumber = matriculationNumber;
        this.tags.addAll(tags);
        this.archived = archived;
        this.payments = Collections.unmodifiableList(new ArrayList<>(payments));
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public boolean isArchived() {
        return archived;
    }

    /**
     * NEW: copy-with for archived flag
     */
    public Person withArchived(boolean newArchived) {
        return new Person(name, phone, email, matriculationNumber, tags, newArchived, payments);
    }

    /**
     * Returns an immutable view of the payments list.
     */
    public List<Payment> getPayments() {
        return payments;
    }

    public MatriculationNumber getMatriculationNumber() {
        return matriculationNumber;
    }

    /**
     * Returns a new Person that is identical to this person but with one extra payment appended.
     * This preserves immutability.
     */
    public Person withAddedPayment(Payment payment) {
        List<Payment> updated = new ArrayList<>(this.payments);
        updated.add(payment);
        return new Person(name, phone, email, matriculationNumber, tags, archived, updated);
    }

    /**
     * Returns a new Person that is identical to this person but with the given payment removed.
     * If the payment does not exist, this person is returned unchanged.
     */
    public Person withRemovedPayment(Payment paymentToRemove) {
        List<Payment> updated = new ArrayList<>(this.payments);
        updated.remove(paymentToRemove);
        return new Person(name, phone, email, matriculationNumber, tags, archived, updated);
    }

    /**
     * Returns a new Person with the payment at {@code zeroBasedPaymentIndex} replaced by {@code edited}.
     */
    public Person withEditedPayment(int zeroBasedPaymentIndex, Payment edited) {
        List<Payment> updated = new ArrayList<>(this.payments);
        updated.set(zeroBasedPaymentIndex, edited);
        return new Person(name, phone, email, matriculationNumber, tags, archived, updated);
    }

    /**
     * Returns true if both persons have the same matriculation number.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        if (otherPerson == null) {
            return false;
        }

        return otherPerson.getMatriculationNumber().equals(getMatriculationNumber());
    }
    /**
     * Returns latest payment if the person has made any pauyment
     */
    public Optional<Payment> getLatestPayment() {
        return getPayments().stream()
                .max(Comparator
                        .comparing(Payment::getDate)
                        .thenComparing(Payment::getRecordedAt)); // if recordedAt exists
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * (Note: payments are intentionally not part of equality to preserve AB3 semantics.)
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Person)) {
            return false;
        }
        Person o = (Person) other;
        return name.equals(o.name)
            && phone.equals(o.phone)
            && email.equals(o.email)
            && matriculationNumber.equals(o.matriculationNumber)
            && tags.equals(o.tags)
            && archived == o.archived;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phone, email, matriculationNumber, tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .add("name", name)
            .add("phone", phone)
            .add("email", email)
            .add("matriculationNumber", matriculationNumber)
            .add("tags", tags)
            .add("archived", archived)
            .add("paymentsCount", payments.size())
            .toString();
    }
}
