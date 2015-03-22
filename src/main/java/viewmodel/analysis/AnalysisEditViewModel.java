package viewmodel.analysis;

import controller.ListSelectionModel;
import controller.database.DatabaseController;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.MultipleSelectionModel;
import model.Account;
import model.Analysis;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
import java.util.stream.Collectors;

import static viewmodel.ViewModelUtils.flattenMessages;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 2/1/15.
 */
public final class AnalysisEditViewModel {
    private final DatabaseController controller;
    private final ResourceBundle resources;
    private StringProperty analysisTitleProperty;
    private StringProperty groupMemberErrors;
    private StringProperty groupNameErrors;
    private ListProperty<Account> accountsProperty;
    private Property<MultipleSelectionModel<Account>> accountSelectionProperty;
    private Analysis analysis;

    @Inject
    public AnalysisEditViewModel(final DatabaseController controller,
                                 @Named("i18n-resources") final ResourceBundle resourceBundle) {
        this.controller = controller;
        this.resources = resourceBundle;
    }

    public StringProperty getAnalysisTitleProperty() {
        if (this.analysisTitleProperty == null) {
            this.analysisTitleProperty = new SimpleStringProperty();
        }
        return this.analysisTitleProperty;
    }

    public ListProperty<Account> getAccountsProperty() {
        if (this.accountsProperty == null) {
            this.accountsProperty = new SimpleListProperty<>();
            final List<Account> accounts = this.controller
                    .getAccounts()
                    .stream()
                    .collect(Collectors.toList());
            this.accountsProperty.setValue(FXCollections.observableList(accounts));
        }
        return this.accountsProperty;
    }

    public Property<MultipleSelectionModel<Account>> getAccountSelectionProperty() {
        if (this.accountSelectionProperty == null) {
            List<Account> items = this.controller.getAccounts()
                    .stream()
                    .collect(Collectors.toList());
            final ListSelectionModel<Account> selectionModel = new ListSelectionModel<>(new ArrayList<>(items));

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

    private Optional<Analysis> synchronizeModel() {
        Optional<Analysis> group = Optional.empty();
        if (this.analysisTitleProperty.getValueSafe() != null) {
            final Analysis analysis = new Analysis(
                    this.analysis.getId().orElse(null),
                    this.analysisTitleProperty.getValueSafe(),
                    this.getAccountSelectionProperty().getValue().getSelectedItems());
            group = Optional.of(analysis);
        }
        return group;
    }

    private boolean validate() {
        final boolean validName = this.validateName();
        final boolean validMembers = this.validateAnalysisMembers();
        return validName && validMembers;
    }

    private boolean validateName() {
        final List<String> messages = new LinkedList<>();
        final String value = this.getAnalysisTitleProperty().getValueSafe();
        if (value.isEmpty()) {
            messages.add(this.resources.getString("DashboardEdit.Needs_name"));
        }
        this.getGroupNameErrors().setValue(flattenMessages(messages));
        return messages.isEmpty();
    }

    private boolean validateAnalysisMembers() {
        final List<String> messages = new LinkedList<>();
        final ObservableList<Account> selected = this.getAccountSelectionProperty().getValue().getSelectedItems();
        if (selected.isEmpty()) {
            messages.add("The group needs accounts to analyze");
        }
        this.getGroupMemberErrors().setValue(flattenMessages(messages));
        return messages.isEmpty();
    }

    public void setAnalysis(final Analysis analysis) {
        this.analysis = analysis;
        analysis.getName().ifPresent(this.getAnalysisTitleProperty()::setValue);
        analysis.getAccounts().forEach(account -> this.getAccountSelectionProperty().getValue().select(account));
    }
}
