package view.dashboard;

import com.cathive.fx.guice.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import viewmodel.dashboard.DashboardViewModel;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 1/31/15.
 */
@FXMLController
public class DashboardView implements Initializable {
    @FXML
    public ComboBox<String> groupsComboBox;
    @Inject
    private DashboardViewModel viewModel;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        this.groupsComboBox.itemsProperty().bindBidirectional(this.viewModel.getGroupsProperty());
    }

    @FXML
    public void createNewGroup(final ActionEvent actionEvent) {
        this.viewModel.addGroup();
    }
}
