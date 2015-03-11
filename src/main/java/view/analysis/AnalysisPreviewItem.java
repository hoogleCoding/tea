package view.analysis;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.BorderPane;
import model.Analysis;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 3/5/15.
 */
public class AnalysisPreviewItem extends ListCell<Analysis> {
    @FXML
    public BorderPane borderPane;

    @FXML
    private Label analysisName;

    public AnalysisPreviewItem(final ResourceBundle resources) {
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("AnalysisPreviewItem.fxml"), resources);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void updateItem(final Analysis item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            item.getName().ifPresent(this.analysisName::setText);
            this.setGraphic(this.borderPane);
        }
    }
}
