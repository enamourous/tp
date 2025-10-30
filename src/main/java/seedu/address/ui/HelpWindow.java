package seedu.address.ui;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;

/**
 * Controller for the Help window.
 * Displays a structured quick command reference and a link to the full User Guide.
 */
public class HelpWindow extends UiPart<Stage> {

    public static final String USERGUIDE_URL =
        "https://ay2526s1-cs2103t-w11-2.github.io/tp/UserGuide.html";

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

    @FXML
    private TextFlow helpTextFlow;

    @FXML
    private Button copyButton;

    /**
     * Creates a new HelpWindow using the given stage.
     *
     * @param root Stage to use as the root of the HelpWindow.
     */
    public HelpWindow(Stage root) {
        super(FXML, root);
    }

    /**
     * Creates a new HelpWindow with a fresh stage.
     */
    public HelpWindow() {
        this(new Stage());
    }

    /**
     * Initializes the HelpWindow after FXML injection.
     * Populates the TextFlow with the help content.
     */
    @FXML
    private void initialize() {
        populateHelpText();
    }

    /**
     * Populates the help text with formatted content.
     */
    private void populateHelpText() {
        helpTextFlow.getChildren().clear();

        addSection("Below is a quick user guide. For detailed explanations, visit:\n", false);
        addSection(USERGUIDE_URL + "\n\n", true);

        addHeader("Member Management");
        addBullet("add n/NAME p/PHONE e/EMAIL m/MATRIC [t/TAG]... — add a new member");
        addBullet("edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [m/MATRIC] [t/TAG]... — edit member details");
        addBullet("list — show all active members");
        addBullet("archive/unarchive INDEX — move members between active and archived lists");
        addBullet("find KEYWORD — search members by name or tag");
        addBullet("view INDEX — view details of a member\n");

        addHeader("Payment Management");
        addBullet("addpayment INDEX[,INDEX]... a/AMOUNT d/DATE [r/REMARKS] — add payment(s)");
        addBullet("editpayment PERSON_INDEX p/PAYMENT_INDEX [a/AMOUNT] [d/DATE] [r/REMARKS] — edit payment");
        addBullet("deletepayment PERSON_INDEX[,PERSON_INDEX]... p/PAYMENT_INDEX — delete payment(s)");
        addBullet("viewpayment INDEX — view payments");
        addBullet("findpayment INDEX [a/AMOUNT] [r/REMARK] [d/DATE] — search a member’s payments\n");

        addHeader("System Commands");
        addBullet("undo — revert the last change");
        addBullet("redo — reapply the last undone change");
        addBullet("help — show this help window");
        addBullet("exit — close the application");
    }

    private void addSection(String text, boolean isLink) {
        Text t = new Text(text);
        t.setStyle(isLink
            ? "-fx-fill: #61afef; -fx-underline: true;"
            : "-fx-fill: white;");
        helpTextFlow.getChildren().add(t);
    }

    private void addHeader(String title) {
        Text header = new Text("\n" + title + "\n");
        header.setStyle("-fx-font-weight: bold; -fx-fill: #ffd700; -fx-font-size: 14px;");
        helpTextFlow.getChildren().add(header);
    }

    private void addBullet(String content) {
        Text bullet = new Text(" • " + content + "\n");
        bullet.setStyle("-fx-fill: white;");
        helpTextFlow.getChildren().add(bullet);
    }

    /**
     * Shows the help window and centers it on screen.
     */
    public void show() {
        logger.fine("Showing help window.");
        getRoot().show();
        getRoot().centerOnScreen();
    }

    /**
     * Returns true if the help window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Hides the help window.
     */
    public void hide() {
        getRoot().hide();
    }

    /**
     * Brings the help window to focus.
     */
    public void focus() {
        getRoot().requestFocus();
    }

    /**
     * Copies the User Guide URL to the clipboard.
     */
    @FXML
    private void copyUrl() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(USERGUIDE_URL);
        clipboard.setContent(content);
    }
}
