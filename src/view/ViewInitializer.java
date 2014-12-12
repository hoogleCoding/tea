package view;

import com.cathive.fx.guice.GuiceApplication;
import com.cathive.fx.guice.GuiceFXMLLoader;
import com.google.inject.Module;
import config.InjectorConfig;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 10/26/14.
 */
public class ViewInitializer extends GuiceApplication {

    @Inject
    private GuiceFXMLLoader fxmlLoader;

    public void run(String... args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        final Parent root = fxmlLoader.load(getClass().getResource("MainWindow.fxml")).getRoot();
        primaryStage.setTitle("Fugger 0.1");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    @Override
    public void init(List<Module> modules) throws Exception {
        modules.add(new InjectorConfig());
    }
}
