package viewmodel.dashboard;

import controller.ListSelectionModel;
import controller.database.DatabaseController;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.MultipleSelectionModel;
import model.AccountGroup;
import view.account.AccountListView;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
import java.util.stream.Collectors;

import static viewmodel.ViewModelUtils.flattenMessages;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 2/1/15.
 */
public class DashboardEditViewModel {
    private final DatabaseController controller;
    private final ResourceBundle resources;
    private StringProperty groupNameProperty;
    private StringProperty groupMemberErrors;
    private StringProperty groupNameErrors;
    private ListProperty<AccountListView> accountsProperty;
    private Property<MultipleSelectionModel<AccountListView>> accountSelectionProperty;

    @Inject
    public DashboardEditViewModel(final DatabaseController controller,
                                  @Named("i18n-resources") final ResourceBundle resourceBundle) {
        this.controller = controller;
        this.resources = resourceBundle;
    }

    public StringProperty getGroupNameProperty() {
        if (this.groupNameProperty == null) {
            this.groupNameProperty = new SimpleStringProperty();
        }
        return this.groupNameProperty;
    }

    public ListProperty<AccountListView> getAccountsProperty() {
        if (this.accountsProperty == null) {
            this.accountsProperty = new SimpleListProperty<>();
            final List<AccountListView> accounts = this.controller
                    .getAccounts()
                    .stream()
                    .map(AccountListView::new)
                    .collect(Collectors.toList());
            this.accountsProperty.setValue(FXCollections.observableList(accounts));
        }
        return this.accountsProperty;
    }

    public Property<MultipleSelectionModel<AccountListView>> getAccountSelectionProperty() {
        if (this.accountSelectionProperty == null) {
            List<AccountListView> items = this.controller.getAccounts()
                    .stream()
                    .map(AccountListView::new)
                    .collect(Collectors.toList());
            final ListSelectionModel<AccountListView> selectionModel = new ListSelectionModel<>(new ArrayList<>(items));

            this.accountSelectionProperty = new SimpleObjectProperty<>(selectionModel);
        }
        return this.accountSelectionProperty;
    }

    public StringProperty getGroupNameErrors() {
        if (this.groupNameErrors == null) {
            this.groupNameErrors = new SimpleStringProperty();
        }
        return this.groupNameErrors;
    }

    public StringProperty getGroupMemberErrors() {
        if (this.groupMemberErrors == null) {
            this.groupMemberErrors = new SimpleStringProperty();
        }
        return this.groupMemberErrors;
    }

    public boolean save() {
        boolean isValid = false;
        if (this.validate()) {
            isValid = this.synchronizeModel()
                    .map(this.controller::save)
                    .isPresent();
        }
        return isValid;
    }

    private Optional<AccountGroup> synchronizeModel() {
        Optional<AccountGroup> group = Optional.empty();
        if (this.groupNameProperty.getValueSafe() != null) {
            final AccountGroup accountGroup = new AccountGroup(this.groupNameProperty.getValueSafe());
            this.getAccountSelectionProperty().getValue().getSelectedItems().stream().forEach(accountGroup::addAccount);
            group = Optional.of(accountGroup);
        }
        return group;
    }

    private boolean validate() {
        final boolean validName = this.validateName();
        final boolean validMembers = this.validateAggregateMembers();
        return validName && validMembers;
    }

    private boolean validateName() {
        final List<String> messages = new LinkedList<>();
        final String value = this.getGroupNameProperty().getValueSafe();
        if (value.isEmpty()) {
            messages.add(this.resources.getString("DashboardEdit.Needs_name"));
        }
        this.getGroupNameErrors().setValue(flattenMessages(messages));
        return messages.isEmpty();
    }

    private boolean validateAggregateMembers() {
        final List<String> messages = new LinkedList<>();
        final ObservableList<AccountListView> selected = this.getAccountSelectionProperty().getValue().getSelectedItems();
        if (selected.isEmpty()) {
            messages.add("The group needs accounts to analyze");
        }
        this.getGroupMemberErrors().setValue(flattenMessages(messages));
        return messages.isEmpty();
    }
}
