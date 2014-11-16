package view;

import controller.DatabaseController;
import controller.DatabaseControllerReceiver;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 11/12/14.
 */
public class ViewLoader {

    public static <T extends Node> T getInitializedView(final String fxml, final DatabaseController controller) {
        if (controller == null) {
            throw new NullPointerException("No database controller for " + fxml);
        }
        final FXMLLoader loader = new FXMLLoader(ViewLoader.class.getResource(fxml));
        T node = null;
        try {
            node = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final DatabaseControllerReceiver receiver = loader.getController();
        receiver.setDatabaseController(controller);
        return node;
    }
}
