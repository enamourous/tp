package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;

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
    private Label latestPayment; // NEW

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

                        // Latest payment text (minimal logic; by date only)
                                String latest = person.getPayments().stream()
                            .max(Comparator.comparing(p -> p.getDate()))
                            .map(p -> {
                            String remark = (p.getRemarks() == null || p.getRemarks().isBlank())
                                            ? "" : " — " + p.getRemarks();
                            return "Latest: " + p.getAmount().toString() + " on " + p.getDate() + remark;
                        })
                            .orElse("Latest: No payments yet");
                latestPayment.setText(latest);
    }
}
