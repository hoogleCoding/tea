package view;

import controller.AccountController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import model.Account;

import java.net.URL;
import java.time.LocalDate;
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

    @FXML
    private DatePicker creationDate;

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
        if (isNotEmpty(this.account.getName())) {
            this.accountName.setText(this.account.getName());
        }
        if (this.account.getCreationTimestamp() == null) {
            this.creationDate.setValue(LocalDate.now());
        } else {
            this.creationDate.setValue(LocalDate.ofEpochDay(this.account.getCreationTimestamp()));
        }

    }

    public void save() {
        if (this.account != null && isNotEmpty(this.accountName.getText())) {
            this.account.setName(this.accountName.getText());
            this.account.setCreationTimestamp(this.creationDate.getValue().toEpochDay());
            this.controller.save(account);
        }
    }
}
