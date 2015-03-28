package control;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 3/21/15.
 */
public class DateSpinner extends HBox implements Initializable {
    private final Animation moveBackAnimation;
    private final Animation moveForwardAnimation;
    @FXML
    private Label dateLabel;
    private Property<LocalDate> dateProperty;

    public DateSpinner() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DateSpinner.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        final double location = this.dateLabel.getLayoutX() - this.dateLabel.getLayoutX();
        this.moveBackAnimation = this.getAnimation(this.dateLabel, location - 40, location);
        this.moveForwardAnimation = this.getAnimation(this.dateLabel, location + 40, location);
    }

    public LocalDate getDate() {
        return this.getDateProperty().getValue();
    }

    public void setDate(final LocalDate date) {
        this.getDateProperty().setValue(date);
        this.dateLabel.setText(date.format(DateTimeFormatter.ofPattern("MMMM yyyy")));
    }

    public Property<LocalDate> getDateProperty() {
        if (this.dateProperty == null) {
            this.dateProperty = new SimpleObjectProperty<>(LocalDate.now());
        }
        return this.dateProperty;
    }

    public void setDateProperty(final Property<LocalDate> dateProperty) {
        this.dateProperty = dateProperty;
    }

    @FXML
    private void moveBack() {
        final LocalDate date = this.getDateProperty().getValue();
        try {
            this.setDate(date.minusMonths(1));
            this.moveBackAnimation.play();
        } catch (final DateTimeException ignored) {
            System.out.println("DateSpinner.moveBack");
        }
    }

    @FXML
    private void moveForward() {
        final LocalDate date = this.getDateProperty().getValue();
        try {
            this.setDate(date.plusMonths(1));
            this.moveForwardAnimation.play();
        } catch (final DateTimeException ignored) {
            System.out.println("DateSpinner.moveForward");
        }
    }

    private Animation getAnimation(final Node object, final double startX, final double endX) {
        final Duration duration = Duration.millis(100);
        final TranslateTransition translateTransition = new TranslateTransition(duration, object);
        translateTransition.setFromX(startX);
        translateTransition.setToX(endX);

        final FadeTransition fadeTransition = new FadeTransition(duration, object);
        fadeTransition.setFromValue(0.2);
        fadeTransition.setToValue(1.0);

        return new ParallelTransition(translateTransition, fadeTransition);
    }

    @FXML
    private void dateLabelClicked(final MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() >= 2) {
            if (this.getDate().isBefore(LocalDate.now())) {
                this.moveForwardAnimation.play();
            } else {
                this.moveBackAnimation.play();
            }
            this.setDate(LocalDate.now());
        }
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
        this.dateLabel.textProperty().bindBidirectional(
                this.getDateProperty(),
                DateTimeFormatter.ofPattern("MMMM yyyy").toFormat());
    }
}
