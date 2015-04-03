package control;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 4/3/15.
 */
public final class FloatingForm extends VBox implements Initializable {

    @FXML
    private Label formLabel;
    private StringProperty headerProperty;

    /**
     * Creates a VBox layout with spacing = 0 and alignment at TOP_LEFT.
     */
    public FloatingForm() {
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FloatingForm.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void setHeader(final String header) {
        this.getHeaderProperty().setValue(header);
    }

    public String getHeader() {
        return this.getHeaderProperty().getValueSafe();
    }

    private StringProperty getHeaderProperty() {
        if (this.headerProperty == null) {
            this.headerProperty = new SimpleStringProperty();
        }
        return this.headerProperty;
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
        this.formLabel.textProperty().bind(this.getHeaderProperty());
    }
}
