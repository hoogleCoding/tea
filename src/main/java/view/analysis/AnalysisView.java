package view.analysis;

import com.cathive.fx.guice.FXMLController;
import control.DateSpinner;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import model.Analysis;
import viewmodel.analysis.AnalysisViewModel;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 3/5/15.
 */
@FXMLController
public class AnalysisView implements Initializable {
    @FXML
    public Label analysisName;
    @FXML
    public PieChart pieChart;
    @FXML
    public DateSpinner dateSpinner;
    @Inject
    private AnalysisViewModel viewModel;

    public void setAnalysis(final Analysis analysis) {
        this.viewModel.setAnalysis(analysis);
        pieChart.setTitle("Monthly Record");
        pieChart.setLegendSide(Side.LEFT);
    }

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.analysisName.textProperty().bind(this.viewModel.getAnalysisNameProperty());
        this.pieChart.dataProperty().bind(this.viewModel.getChartDataProperty());
        this.dateSpinner.getDateProperty().bindBidirectional(this.viewModel.getAnalysisDateProperty());
    }
}
