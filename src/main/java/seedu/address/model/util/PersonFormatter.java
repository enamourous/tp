package seedu.address.model.util;

import seedu.address.model.person.Person;
import seedu.address.model.payment.Payment;

// additional PersonFormatter class for better abstraction
public class PersonFormatter {
    /**
     * Returns a human-readable formatted profile string for the given person.
     */
    public static String formatProfile(Person person) {
        StringBuilder sb = new StringBuilder();

        sb.append("Name: ").append(person.getName()).append("\n");
        sb.append("Phone: ").append(person.getPhone()).append("\n");
        sb.append("Email: ").append(person.getEmail()).append("\n");
        sb.append("Matriculation No.: ").append(person.getMatriculationNumber()).append("\n");

        if (person.getTags().isEmpty()) {
            sb.append("Tags: (none)\n");
        } else {
            sb.append("Tags: ").append(person.getTags()).append("\n");
        }

        sb.append("Status: ").append(person.isArchived() ? "Archived" : "Active").append("\n");

        sb.append("Payments: ").append(person.getPayments().size()).append(" total");

        // If available, include latest payment info
        person.getLatestPayment().ifPresent(latest ->
                sb.append(" (Latest on ").append(latest.getDate()).append(")").append("\n")
        );

        sb.append("To view the full list of payments, use the \"viewpayments\" command.");

        return sb.toString();
    }
}
