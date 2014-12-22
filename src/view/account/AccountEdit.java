package view.account;

import com.cathive.fx.guice.FXMLController;
import controller.database.DatabaseController;
import controller.layout.OverlayProvider;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import model.Account;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 10/24/14.
 */
@FXMLController
public class AccountEdit {
    //<editor-fold desc="FXML fields">
    @FXML
    public Label accountNameError;
    @FXML
    public Tooltip accountNameErrorTooltip;
    @FXML
    public TextArea description;
    @FXML
    private TextField accountName;
    //</editor-fold>
    @Inject
    private OverlayProvider overlayProvider;
    @Inject
    private DatabaseController controller;
    private Account account = null;

    public Account getAccount() {
        return account;
    }

    public void setAccount(final Account account) {
        this.account = account;
        if (isNotEmpty(this.account.getName())) {
            this.accountName.setText(this.account.getName());
            this.description.setText(this.account.getDescription());
        }
    }

    public void save() {
        if (this.account != null && this.validate()) {
            this.account.setName(this.accountName.getText());
            this.account.setDescription(this.description.getText());
            this.controller.save(account);
            this.overlayProvider.dispose();
        }
    }

    public void cancel(final ActionEvent actionEvent) {
        this.overlayProvider.dispose();
    }

    private boolean validate() {
        return this.validateAccountName();
    }

    private boolean validateAccountName() {
        final List<String> messages = new LinkedList<>();
        if (this.accountName.getText().isEmpty()) {
            messages.add("The account needs a name");
        }
        if (messages.isEmpty()) {
            this.accountNameError.visibleProperty().setValue(false);
            this.accountNameErrorTooltip.setText("");
            this.accountName.getStyleClass().remove("error");
        } else {
            this.accountNameError.visibleProperty().setValue(true);
            this.accountName.getStyleClass().add("error");
            this.accountNameErrorTooltip.setText(
                    messages
                            .stream()
                            .reduce("", (a, b) -> String.format("%s\n%s", a, b)).replaceFirst("\n", ""));
        }
        return messages.isEmpty();
    }
}
