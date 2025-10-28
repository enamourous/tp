package seedu.address.ui;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;

/**
 * Controller for the Help window.
 * Displays a structured quick command reference and a link to the full User Guide.
 */
public class HelpWindow extends UiPart<Stage> {
    public static final String USERGUIDE_URL = "https://ay2526s1-cs2103t-w11-2.github.io/tp/UserGuide.html";

    // organized help message for quick user reference
    public static final String HELP_MESSAGE =
            "Below is a quick user guide. For detailed explanations, visit:\n" + USERGUIDE_URL + "\n"
                    + "──────────────────────────────\n"
                    + "BASIC COMMANDS\n"
                    + "**Member Management**\n"
                    + "  add n/NAME p/PHONE e/EMAIL m/MATRIC [t/TAG]... — add a new member\n"
                    + "  edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [m/MATRIC] [t/TAG]... — edit member details\n"
                    + "  list — show all active members\n"
                    + "  listarchived — show archived members\n"
                    + "  archive INDEX[,INDEX]... — archive one or more members\n"
                    + "  unarchive INDEX[,INDEX]... — restore archived members\n"
                    + "  find KEYWORD [MORE_KEYWORDS]... — search members by name or tag\n"
                    + "  view INDEX — view details of a member\n\n"

                    + "**Payment Management**\n"
                    + "  addpayment INDEX[,INDEX]... a/AMOUNT d/DATE [r/REMARKS] — add payment(s)\n"
                    + "  editpayment PERSON_INDEX p/PAYMENT_INDEX [a/AMOUNT] [d/DATE] [r/REMARKS] — edit payment\n"
                    + "  deletepayment PERSON_INDEX[,PERSON_INDEX]... p/PAYMENT_INDEX — delete payment(s)\n"
                    + "  viewpayment INDEX | all — view payment(s)\n"
                    + "  findpayment INDEX [a/AMOUNT] [r/REMARK] [d/DATE] — search a member’s payments\n\n"

                    + "**System Commands**\n"
                    + "  undo — revert the last change\n"
                    + "  redo — reapply the last undone change\n"
                    + "  help — show this help window\n"
                    + "  exit — close the application\n\n"

                    + "Tip: Run 'list' or 'listarchived' before commands that use an INDEX.\n";


    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

    @FXML
    private Button copyButton;

    @FXML
    private Label helpMessage;

    /**
     * Creates a new HelpWindow using the given stage as the root.
     *
     * @param root Stage to use as the root of the HelpWindow.
     */
    public HelpWindow(Stage root) {
        super(FXML, root);
        helpMessage.setText(HELP_MESSAGE);
    }

    /**
     * Creates a new HelpWindow.
     */
    public HelpWindow() {
        this(new Stage());
    }

    /**
     * Shows the help window.
     *
     * @throws IllegalStateException if called from a non-JavaFX thread or when the window is already showing.
     */
    public void show() {
        logger.fine("Showing help window with command reference.");
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
     * Focuses on the help window.
     */
    public void focus() {
        getRoot().requestFocus();
    }

    /**
     * Copies the URL to the User Guide to the system clipboard.
     */
    @FXML
    private void copyUrl() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(USERGUIDE_URL);
        clipboard.setContent(content);
    }
}
