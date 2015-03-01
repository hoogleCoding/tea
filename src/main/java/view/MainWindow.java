package view;

import com.cathive.fx.guice.GuiceFXMLLoader;
import controller.layout.OverlayProvider;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.layout.FlowPane;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 10/31/14.
 */
@Singleton
public class MainWindow implements Initializable, OverlayProvider {
    @FXML
    public Tab dashboardTab;
    @FXML
    private FlowPane overlayPane;
    @Inject
    private GuiceFXMLLoader fxmlLoader;
    @FXML
    private Tab accountsTab;
    @FXML
    private Tab transactionTab;
    @Inject
    @Named("i18n-resources")
    private ResourceBundle resources;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            this.dashboardTab
                    .setContent(
                            this.fxmlLoader
                                    .load(getClass().getResource("dashboard/DashboardView.fxml"), this.resources)
                                    .getRoot()
                    );
            this.accountsTab
                    .setContent(
                            fxmlLoader
                                    .load(getClass().getResource("account/AccountListingView.fxml"), this.resources)
                                    .getRoot()
                    );
            this.transactionTab
                    .setContent(
                            fxmlLoader
                                    .load(getClass().getResource("transaction/TransactionListingView.fxml"), this.resources)
                                    .getRoot()
                    );
        } catch (IOException e) {
            //TODO: Log something
            e.printStackTrace();
        }
    }

    @Override
    public boolean show(Node node) {
        if (!this.overlayPane.getChildren().isEmpty()) {
            return false;
        }
        this.overlayPane.getChildren().add(node);
        this.overlayPane.setVisible(true);
        return true;
    }

    @Override
    public void dispose() {
        this.overlayPane.getChildren().clear();
        this.overlayPane.setVisible(false);
    }
}
