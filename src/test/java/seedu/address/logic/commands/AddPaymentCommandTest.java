package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.payment.Amount;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Test scheme:
 * Assuming the model stub with person is correctly created:
 * 1. add payment to single person successfully without remarks
 * 2. add payment to single person successfully with remarks
 * 3. add payment to multiple persons successfully without remarks
 * 4. add payment to multiple persons successfully with remarks
 * 5. add payment to multiple persons with invalid index(es)
 * 6. add payment to single person with missing date
 * 7. add payment to single person with missing amount
 * 8. add payment to single person with incorrect date
 * 9. add payment to single person with incorrect amount
 */

//public class AddPaymentCommandTest {
//    // 1. Single person, no remarks
//    @Test
//    public void execute_singlePerson_noRemarks_success() throws Exception {
//        Person bob = new PersonBuilder().withName("Bob").build();
//        Model model = new ModelStubWithPerson(bob);
//
//        AddPaymentCommand command = new AddPaymentCommand(
//                List.of(Index.fromOneBased(1)),
//                new Amount(new BigDecimal("66.66")),
//                LocalDate.of(2025, 10, 10),
//                null // no remarks
//        );
//
//        CommandResult result = command.execute(model);
//    }
//}
