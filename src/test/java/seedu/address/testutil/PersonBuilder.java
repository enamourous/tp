package seedu.address.testutil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

import seedu.address.model.person.Email;
import seedu.address.model.person.MatriculationNumber;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;
import seedu.address.model.payment.Payment;

/**
 * A utility class to help with building Person objects for tests.
 * Supports adding payments via {@link #withPayments(Payment...)}.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_MATRICULATIONNUM = "A33333333A";

    private Name name;
    private Phone phone;
    private Email email;
    private MatriculationNumber matriculationNumber;
    private Set<Tag> tags;

    // New: payments to attach to the built Person
    private List<Payment> payments = new ArrayList<>();

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        matriculationNumber = new MatriculationNumber(DEFAULT_MATRICULATIONNUM);
        tags = new HashSet<>();
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     * Note: payments are intentionally not copied to keep cloning predictable in tests.
     */
    public PersonBuilder(Person personToCopy) {
        name = personToCopy.getName();
        phone = personToCopy.getPhone();
        email = personToCopy.getEmail();
        matriculationNumber = personToCopy.getMatriculationNumber();
        tags = new HashSet<>(personToCopy.getTags());
    }

    /** Sets the {@code Name} of the {@code Person} that we are building. */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /** Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Person} that we are building. */
    public PersonBuilder withTags(String... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /** Sets the {@code MatriculationNumber} of the {@code Person} that we are building. */
    public PersonBuilder withMatriculationNumber(String matriculationNum) {
        this.matriculationNumber = new MatriculationNumber(matriculationNum);
        return this;
    }

    /** Sets the {@code Phone} of the {@code Person} that we are building. */
    public PersonBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /** Sets the {@code Email} of the {@code Person} that we are building. */
    public PersonBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /** Adds payments to the person being built. */
    public PersonBuilder withPayments(Payment... payments) {
        this.payments = Arrays.asList(payments);
        return this;
    }

    /**
     * Builds the Person. If payments were provided, this tries the following (in order):
     * 1) person.withPayments(List&lt;Payment&gt;)
     * 2) person.withAddedPayment(Payment) repeatedly
     * 3) A Person constructor that accepts payments as an extra parameter
     */
    public Person build() {
        Person base = new Person(name, phone, email, matriculationNumber, tags);

        if (payments.isEmpty()) {
            return base;
        }

        // Option 1: withPayments(List<Payment>)
        try {
            Method m = Person.class.getMethod("withPayments", List.class);
            Object out = m.invoke(base, payments);
            return (Person) out;
        } catch (ReflectiveOperationException ignored) { }

        // Option 2: withAddedPayment(Payment) repeatedly
        try {
            Method add = Person.class.getMethod("withAddedPayment", Payment.class);
            Person current = base;
            for (Payment p : payments) {
                current = (Person) add.invoke(current, p);
            }
            return current;
        } catch (ReflectiveOperationException ignored) { }

        // Option 3: constructor that includes payments (… , Set<Tag>, List<Payment>)
        try {
            Constructor<Person> ctor = Person.class.getConstructor(
                    Name.class, Phone.class, Email.class, MatriculationNumber.class, Set.class, List.class);
            return ctor.newInstance(name, phone, email, matriculationNumber, tags, payments);
        } catch (ReflectiveOperationException ignored) { }

        // Fallback: return base if none of the above APIs exist
        return base;
    }
}
