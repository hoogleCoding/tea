package controller;

import controller.database.Database;
import controller.database.DatabaseController;
import model.Account;
import model.Transaction;
import org.junit.Test;

import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 10/28/14.
 */
public class DatabaseControllerTest {

    @Test
    public void itShouldRunObserversWhenAnAccountIsSaved() {
        final DatabaseController controller = new DatabaseController(mock(Database.class));
        final boolean[] isCalled = {false};
        controller.addAccountChangeListener(x -> isCalled[0] = true);
        controller.save(mock(Account.class));
        assertTrue("The account controller should inform its listeners of a save.", isCalled[0]);
    }

    @Test
    public void itShouldRunObserversWhenATransactionIsSaved() {
        final DatabaseController controller = new DatabaseController(mock(Database.class));
        final boolean[] isCalled = {false};
        controller.addTransactionChangeListener(x -> isCalled[0] = true);
        Transaction transaction = mock(Transaction.class);
        when(transaction.getId()).thenReturn(Optional.<Long>empty());
        controller.save(transaction);
        assertTrue("The transaction controller should inform its listeners of a save.", isCalled[0]);
    }
}
