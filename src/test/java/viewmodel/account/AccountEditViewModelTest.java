package viewmodel.account;

import com.google.inject.Guice;
import config.InjectorConfig;
import model.Account;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 3/11/15.
 */
public class AccountEditViewModelTest {
    private AccountEditViewModel viewModel;
    private File tempDB;

    @Before
    public void setUp() throws Exception {
        final InjectorConfig config = new InjectorConfig();
        this.tempDB = File.createTempFile("fugger", "db");
        config.setDataBasePath(tempDB);
        this.viewModel = Guice.createInjector(config).getInstance(AccountEditViewModel.class);
    }

    @After
    public void tearDown() throws Exception {
        this.tempDB.delete();
    }

    @Test
    public void itShouldSaveNewAccounts() throws Exception {
        this.viewModel.setAccount(new Account());
        this.viewModel.getNameProperty().setValue("TestAccount");
        this.viewModel.getCurrencyProperty().setValue("CHF");
        assertTrue("It should return true on a correct save", this.viewModel.save());
    }

    @Test
    public void itShouldNotSaveInvalidAccounts() throws Exception{
        //TODO: implement this
        assertFalse(true);
    }
}
