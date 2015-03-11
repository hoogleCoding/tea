package view.analysis;

import com.cathive.fx.guice.FXMLController;
import controller.layout.OverlayProvider;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.Account;
import model.Analysis;
import view.account.AccountListCell;
import viewmodel.analysis.AnalysisEditViewModel;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 2/1/15.
 */
@FXMLController
public class AnalysisEditView implements Initializable {
    @FXML
    public Tooltip groupErrorTooltip;
    @FXML
    public Label groupError;
    @FXML
    public ListView<Account> groupMembers;
    @FXML
    public Label groupMemberError;
    @FXML
    public Tooltip groupMemberErrorTooltip;
    @FXML
    private TextField groupName;
    @Inject
    private AnalysisEditViewModel viewModel;
    @Inject
    private OverlayProvider overlayProvider;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        this.groupName.textProperty().bindBidirectional(this.viewModel.getGroupNameProperty());
        this.groupErrorTooltip.textProperty().bind(this.viewModel.getGroupNameErrors());
        this.groupError.visibleProperty().bind(this.viewModel.getGroupNameErrors().isNotEmpty());
        this.groupMembers.setCellFactory(a -> new AccountListCell());
        this.groupMembers.itemsProperty().bind(this.viewModel.getAccountsProperty());
        this.groupMembers.selectionModelProperty().bindBidirectional(this.viewModel.getAccountSelectionProperty());
        this.groupMembers.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.groupMemberError.visibleProperty().bind(this.viewModel.getGroupMemberErrors().isNotEmpty());
        this.groupMemberErrorTooltip.textProperty().bind(this.viewModel.getGroupMemberErrors());
    }

    public void save(final ActionEvent actionEvent) {
        if (this.viewModel.save()) {
            this.overlayProvider.dispose();
        }
    }

    public void cancel(final ActionEvent actionEvent) {
        this.overlayProvider.dispose();
    }

    public void setAnalysis(final Analysis analysis) {
        this.viewModel.setAnalysis(analysis);
    }
}
