package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditPaymentCommand.EditPaymentDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PaymentBuilder;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.TypicalPersons;

public class AddEditViewPaymentFlowIntegrationTest {

    @Test
    public void addEditView_endToEnd_success() throws Exception {
        Model model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());

        Person personToAdd = new PersonBuilder()
                .withName("Zed Flow")
                .withPayments(new PaymentBuilder()
                        .withAmount("12.34")
                        .withDate("2025-01-01")
                        .withRemarks("initial")
                        .build())
                .build();

        CommandResult addResult = new AddMemberCommand(personToAdd).execute(model);
        assertTrue(addResult.getFeedbackToUser().toLowerCase().contains("added"));

        Index personIndex = Index.fromOneBased(model.getFilteredPersonList().size());

        EditPaymentDescriptor descriptor = new EditPaymentDescriptor();
        descriptor.setRemarks("updated-remarks");
        CommandResult editResult = new EditPaymentCommand(personIndex, 1, descriptor).execute(model);
        assertTrue(editResult.getFeedbackToUser().toLowerCase().contains("edited"));

        CommandResult viewResult = new ViewPaymentCommand(personIndex).execute(model);
        assertTrue(viewResult.getFeedbackToUser().toLowerCase().contains("updated-remarks"));
    }

    @Test
    public void addEditView_onFreshModel_success() throws Exception {
        Model model = new ModelManager(new AddressBook(), new UserPrefs());

        Person personToAdd = new PersonBuilder()
                .withName("Flow Fresh")
                .withPayments(new PaymentBuilder()
                        .withAmount("10.00")
                        .withDate("2025-02-02")
                        .withRemarks("orig")
                        .build())
                .build();

        new AddMemberCommand(personToAdd).execute(model);
        Index idx = Index.fromOneBased(model.getFilteredPersonList().size());

        EditPaymentDescriptor descriptor = new EditPaymentDescriptor();
        descriptor.setRemarks("changed");
        new EditPaymentCommand(idx, 1, descriptor).execute(model);

        CommandResult view = new ViewPaymentCommand(idx).execute(model);
        assertTrue(view.getFeedbackToUser().toLowerCase().contains("changed"));
    }
}
