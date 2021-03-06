package view;

import com.cathive.fx.guice.GuiceApplication;
import com.cathive.fx.guice.GuiceFXMLLoader;
import com.google.inject.Module;
import config.InjectorConfig;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.inject.Inject;
import javax.inject.Named;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 10/26/14.
 */
public class ViewInitializer extends GuiceApplication {

    @Inject
    private GuiceFXMLLoader fxmlLoader;
    @Inject
    @Named("i18n-resources")
    private ResourceBundle resourceBundle;

    public void run(String... args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        final URL resource = getClass().getResource("MainWindow.fxml");
        final Parent root = fxmlLoader.load(resource, resourceBundle).getRoot();
        primaryStage.setTitle("Fugger 0.1");
        primaryStage.getIcons().add(new Image("icon/icon_16.png"));
        primaryStage.getIcons().add(new Image("icon/icon_24.png"));
        primaryStage.getIcons().add(new Image("icon/icon_32.png"));
        primaryStage.getIcons().add(new Image("icon/icon_48.png"));
        primaryStage.getIcons().add(new Image("icon/icon_64.png"));
        primaryStage.getIcons().add(new Image("icon/icon_96.png"));
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    @Override
    public void init(List<Module> modules) throws Exception {
        modules.add(new InjectorConfig());
    }
}
