package viewmodel.account;

import controller.database.DatabaseController;
import model.Account;
import model.Transaction;
import org.junit.Before;
import org.junit.Test;

import javax.money.MonetaryCurrencies;
import java.util.Collections;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AccountEditViewModelTest {

    private final ResourceBundle resourceBundle;
    private AccountEditViewModel accountEditViewModel;
    private DatabaseController databaseController;
    private Account account;

    public AccountEditViewModelTest() {
        this.resourceBundle = ResourceBundle.getBundle("fugger", Locale.forLanguageTag("en"));
    }

    @Before
    public void setUp() {
        this.account = new Account(1L, "test");
        this.account.setCurrency(MonetaryCurrencies.getCurrency("CHF"));
        this.databaseController = mock(DatabaseController.class);
        this.accountEditViewModel = new AccountEditViewModel(this.databaseController, this.resourceBundle);
        this.accountEditViewModel.setAccount(this.account);
    }

    @Test
    public void itShouldAllowCurrencyChangeOnAccountsWithNoTransactions() {
        when(this.databaseController.getTransactions(this.account)).thenReturn(Collections.emptyList());
        this.accountEditViewModel.getNameProperty().setValue(this.account.getName().get());
        this.accountEditViewModel.getCurrencyProperty().setValue("BTC");
        assertTrue(
                "Changing the currency of an account which has no transactions is permitted",
                this.accountEditViewModel.validate());
    }

    @Test
    public void itShouldNotAllowCurrencyChangeIfAccountHasTransactions() {
        when(this.databaseController.getTransactions(this.account)).thenReturn(Collections.singleton(mock(Transaction.class)));
        this.accountEditViewModel.getNameProperty().setValue(this.account.getName().get());
        this.accountEditViewModel.getCurrencyProperty().setValue("BTC");
        assertFalse(
                "Changing the currency of an account which has transactions is not permitted",
                this.accountEditViewModel.validate());
    }
}