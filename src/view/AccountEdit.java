package view;

import controller.AccountController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import model.Account;

import java.net.URL;
import java.util.ResourceBundle;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 10/24/14.
 */
public class AccountEdit implements Initializable {
    private final AccountController controller;
    private Account account = null;

    @FXML
    private TextField accountName;

    public AccountEdit(final AccountController controller) {
        this.controller = controller;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(final Account account) {
        this.account = account;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (isNotEmpty(account.getName())) {
            this.accountName.setText(this.account.getName());
        }
    }

    public void save() {
        if (this.account != null && isNotEmpty(this.accountName.getText())) {
            this.account.setName(this.accountName.getText());
            this.controller.save(account);
        }
    }
}
