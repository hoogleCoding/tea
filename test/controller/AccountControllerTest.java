package controller;

import model.Account;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 10/28/14.
 */
public class AccountControllerTest {

    @Test
    public void itShouldRunObserversWhenAnAccountIsSaved() {
        final AccountController controller = new AccountController(mock(Database.class));
        final boolean[] isCalled = {false};
        controller.addChangeListener(x -> isCalled[0] = true);
        controller.save(mock(Account.class));
        assertTrue("The account controller should inform its listeners of a save.", isCalled[0]);
    }
}
