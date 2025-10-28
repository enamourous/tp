package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;

/**
 * A UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label matriculationNum;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;
    @FXML
    private Label archivedLabel;
    @FXML
    private Label latestPayment;

    /**
     * Creates a {@code PersonCard} with the given {@code Person} and index to display.
     *
     * @param person the person whose details to show
     * @param displayedIndex the 1-based index to display on the card
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        matriculationNum.setText(person.getMatriculationNumber().value);
        email.setText(person.getEmail().value);

        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));

        if (person.isArchived()) {
            archivedLabel.setVisible(true);
            archivedLabel.setText("Archived");
        } else {
            archivedLabel.setVisible(false);
        }

        // Latest payment line (by date)
        String latest = person.getPayments().stream()
                .max(Comparator.comparing(p -> p.getDate()))
                .map(p -> {
                    String remark = (p.getRemarks() == null || p.getRemarks().isBlank())
                            ? "" : " for " + p.getRemarks();
                    return "Latest Payment: $" + p.getAmount().toString()
                            + " on " + p.getDate() + remark;
                })
                .orElse("Latest Payment: No payments yet");
        latestPayment.setText(latest);
    }
}
