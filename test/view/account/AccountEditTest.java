package view.account;

import controller.database.DatabaseController;
import controller.layout.OverlayProvider;
import javafx.scene.control.TextField;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AccountEditTest {

    private AccountEdit accountEdit;

    @Before
    public void setUp() {
        this.accountEdit = new AccountEdit();
        this.accountEdit.overlayProvider = mock(OverlayProvider.class);
        this.accountEdit.controller = mock(DatabaseController.class);
        this.accountEdit.accountName = mock(TextField.class);
    }

    @Test
    public void itShouldNotValidateMissingAccountNames() {
        when(this.accountEdit.accountName.getText()).thenReturn("");
        assertFalse("Empty account names cannot be valid", this.accountEdit.validateAccountName());

    }

}