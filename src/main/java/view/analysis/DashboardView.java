package view.analysis;

import com.cathive.fx.guice.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.Analysis;
import viewmodel.analysis.DashboardViewModel;

import javax.inject.Inject;
import javax.inject.Named;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 1/31/15.
 */
@FXMLController
public class DashboardView implements Initializable {
    @FXML
    public ComboBox<String> groupsComboBox;
    @FXML
    public ListView<Analysis> analysisPreview;
    @FXML
    public AnchorPane currentAnalysis;
    @Inject
    @Named("i18n-resources")
    private ResourceBundle resources;
    @Inject
    private DashboardViewModel viewModel;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        this.analysisPreview.itemsProperty().bind(this.viewModel.getAnalysesProperty());
        this.analysisPreview.setCellFactory(callback -> new AnalysisPreviewItem(this.resources));
        this.currentAnalysis.getChildren().add(this.viewModel.getCurrentAnalysis());
        try {
            final Analysis defaultAnalysis = this.viewModel.getAnalysesProperty().get(0);
            this.viewModel.setSelectedAnalysis(defaultAnalysis);
        } catch (IndexOutOfBoundsException ignored) {
        }
    }

    @FXML
    public void createNewGroup(final ActionEvent actionEvent) {
        this.viewModel.addAnalysis();
    }

    public void previewClicked(final MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            final Analysis selectedItem = this.analysisPreview.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                this.viewModel.setSelectedAnalysis(selectedItem);
                if (event.getClickCount() >= 2) {
                    this.viewModel.editAnalysis(selectedItem);
                }
            }
        }
    }
}
