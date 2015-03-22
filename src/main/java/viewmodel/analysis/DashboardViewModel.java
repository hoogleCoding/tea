package viewmodel.analysis;

import com.cathive.fx.guice.GuiceFXMLLoader;
import controller.database.DatabaseController;
import controller.layout.OverlayProvider;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import model.Analysis;
import view.analysis.AnalysisEditView;
import view.analysis.AnalysisView;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 1/31/15.
 */
public class DashboardViewModel {
    private final OverlayProvider overlayProvider;
    private final ResourceBundle resources;
    private final GuiceFXMLLoader fxmlLoader;
    private final DatabaseController databaseController;
    private ListProperty<Analysis> analysesProperty;
    private AnalysisView analysisController;

    @Inject
    public DashboardViewModel(final GuiceFXMLLoader fxmlLoader,
                              final OverlayProvider overlayProvider,
                              final DatabaseController databaseController,
                              @Named("i18n-resources") final ResourceBundle resources) {
        this.overlayProvider = overlayProvider;
        this.resources = resources;
        this.fxmlLoader = fxmlLoader;
        this.databaseController = databaseController;
        this.databaseController.addAnalysisChangeListener(
                analysis -> this.getAnalysesProperty().setValue(this.getAnalyses())
        );
    }

    public ListProperty<Analysis> getAnalysesProperty() {
        if (this.analysesProperty == null) {
            this.analysesProperty = new SimpleListProperty<>();
            this.analysesProperty.setValue(this.getAnalyses());
        }
        return this.analysesProperty;
    }

    private ObservableList<Analysis> getAnalyses() {
        final List<Analysis> analyses = this.databaseController
                .getAccountGroups()
                .stream()
                .collect(Collectors.toList());
        analyses.add(new Analysis(""));
        return FXCollections.observableList(analyses);

    }

    public void addAnalysis() {
        try {
            final GuiceFXMLLoader.Result result = this.fxmlLoader.load(
                    getClass().getResource("/view/analysis/AnalysisEditView.fxml"),
                    this.resources);
            this.overlayProvider.show(result.getRoot());
        } catch (IOException e) {
            //TODO: log
            e.printStackTrace();
        }
    }

    public void setSelectedAnalysis(final Analysis selectedItem) {
        this.analysisController.setAnalysis(selectedItem);
    }

    public Node getCurrentAnalysis() {
        Node analysis = null;
        try {
            final GuiceFXMLLoader.Result result = this.fxmlLoader.load(
                    getClass().getResource("/view/analysis/AnalysisView.fxml"),
                    this.resources);
            this.analysisController = result.getController();
            analysis = result.getRoot();
        } catch (IOException e) {
            //TODO: log
            e.printStackTrace();
        }
        return analysis;
    }

    public void editAnalysis(final Analysis analysis) {
        try {
            final GuiceFXMLLoader.Result result = this.fxmlLoader.load(
                    getClass().getResource("/view/analysis/AnalysisEditView.fxml"),
                    resources);
            ((AnalysisEditView) result.getController()).setAnalysis(analysis);
            this.overlayProvider.show(result.getRoot());
        } catch (IOException e) {
            //TODO: log
            e.printStackTrace();
        }
    }
}
