package seedu.address.testutil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet; // must be before List (lexicographic)
import java.util.List;
import java.util.Set;

import seedu.address.model.payment.Payment; // must be before SampleDataUtil
import seedu.address.model.person.Email;
import seedu.address.model.person.MatriculationNumber;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects for tests.
 * Supports adding payments via {@link #withPayments(Payment...)}.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_MATRICULATIONNUM = "A3333333A";

    private Name name;
    private Phone phone;
    private Email email;
    private MatriculationNumber matriculationNumber;
    private Set<Tag> tags;

    // Payments to attach to the built Person (optional)
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
     * Falls back to base person if none exist.
     */
    public Person build() {
        Person base = new Person(name, phone, email, matriculationNumber, tags);

        if (payments.isEmpty()) {
            return base;
        }

        // Option 1: withPayments(List<Payment>)
        Method withPaymentsMethod = findMethod(Person.class, "withPayments", List.class);
        if (withPaymentsMethod != null) {
            try {
                Object out = withPaymentsMethod.invoke(base, payments);
                return (Person) out;
            } catch (ReflectiveOperationException e) {
                // Fail fast if the method exists but invocation failed.
                throw new RuntimeException("Invoking Person.withPayments(List<Payment>) failed", e);
            }
        }

        // Option 2: withAddedPayment(Payment) repeatedly
        Method withAddedPaymentMethod = findMethod(Person.class, "withAddedPayment", Payment.class);
        if (withAddedPaymentMethod != null) {
            try {
                Person current = base;
                for (Payment p : payments) {
                    current = (Person) withAddedPaymentMethod.invoke(current, p);
                }
                return current;
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException("Invoking Person.withAddedPayment(Payment) failed", e);
            }
        }

        // Option 3: constructor that includes payments (… , Set<Tag>, List<Payment>)
        Constructor<Person> ctor = findConstructor(Person.class,
                Name.class, Phone.class, Email.class, MatriculationNumber.class, Set.class, List.class);
        if (ctor != null) {
            try {
                return ctor.newInstance(name, phone, email, matriculationNumber, tags, payments);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException("Invoking Person constructor with payments failed", e);
            }
        }

        // Fallback
        return base;
    }

    // --- Reflection helpers that probe without empty catch blocks ---

    private static Method findMethod(Class<?> cls, String name, Class<?>... paramTypes) {
        try {
            return cls.getMethod(name, paramTypes);
        } catch (NoSuchMethodException e) {
            return null; // method doesn't exist -> try next strategy
        }
    }

    private static <T> Constructor<T> findConstructor(Class<T> cls, Class<?>... paramTypes) {
        try {
            return cls.getConstructor(paramTypes);
        } catch (NoSuchMethodException e) {
            return null; // ctor doesn't exist -> try next strategy
        }
    }
}
