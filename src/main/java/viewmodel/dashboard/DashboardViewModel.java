package viewmodel.dashboard;

import com.cathive.fx.guice.GuiceFXMLLoader;
import controller.database.Database;
import controller.database.DatabaseController;
import controller.layout.OverlayProvider;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import model.AccountGroup;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 1/31/15.
 */
public class DashboardViewModel {
    private final OverlayProvider overlayProvider;
    private final ResourceBundle resources;
    private final GuiceFXMLLoader fxmlLoader;
    private final DatabaseController database;
    private ListProperty<String> groupsProperty;

    @Inject
    public DashboardViewModel(final GuiceFXMLLoader fxmlLoader,
                              final OverlayProvider overlayProvider,
                              final DatabaseController database,
                              @Named("i18n-resources") final ResourceBundle resources) {
        this.overlayProvider = overlayProvider;
        this.resources = resources;
        this.fxmlLoader = fxmlLoader;
        this.database = database;
    }

    public ListProperty<String> getGroupsProperty() {
        if (this.groupsProperty == null) {
            this.groupsProperty = new SimpleListProperty<>();
            final List<String> groups = this.database
                    .getAccountGroups()
                    .stream()
                    .map(AccountGroup::getName)
                    .map(Optional::get)
                    .collect(Collectors.toList());
            this.groupsProperty.setValue(FXCollections.observableList(groups));
        }
        return this.groupsProperty;
    }

    public void addGroup() {
        try {
            final GuiceFXMLLoader.Result result = this.fxmlLoader.load(
                    getClass().getResource("/view/dashboard/DashboardEditView.fxml"),
                    this.resources);
            this.overlayProvider.show(result.getRoot());
        } catch (IOException e) {
            //TODO: log
            e.printStackTrace();
        }
    }
}
