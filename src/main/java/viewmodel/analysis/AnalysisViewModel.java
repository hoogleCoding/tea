package viewmodel.analysis;

import controller.MoneyHelper;
import controller.database.DatabaseController;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import model.Account;
import model.Analysis;
import org.javamoney.moneta.FastMoney;

import javax.inject.Inject;
import javax.money.MonetaryAmount;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 3/5/15.
 */
public final class AnalysisViewModel {
    private final DatabaseController databaseController;
    private StringProperty analysisName;
    private ListProperty<PieChart.Data> chartData;
    private Analysis analysis;
    private Property<LocalDate> analysisDate;

    @Inject
    public AnalysisViewModel(final DatabaseController databaseController) {
        this.databaseController = databaseController;
        this.databaseController.addAnalysisChangeListener(this::setAnalysis);
    }

    public void setAnalysis(final Analysis analysis) {
        if (!analysis.equals(this.analysis)) {
            this.analysis = analysis;
            this.updateName();
            this.updateChartData();
        }
    }

    private void updateChartData() {
        List<PieChart.Data> data = this.analysis.getAccounts()
                .stream()
                .map(account -> new PieChart.Data(account.getName().orElse("N/A"), this.getAccountBalance(account, this.analysisDate.getValue())))
                .collect(Collectors.toList());

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(data);
        this.getChartDataProperty().set(pieChartData);
    }

    private void updateName() {
        this.analysis.getName().ifPresent(this.getAnalysisNameProperty()::setValue);
    }

    /**
     * Gets the balance of an account of a month and year. Albeit a date is given this method uses just the month and
     * year part of it. All transactions concerning the account from the first until and including the last day of the
     * month are taken into account.
     *
     * @param account The account to get the balance of.
     * @param date    The date to get the balance of. This uses just the month and year part of the date.
     * @return The account's balance during the specified month.
     */
    private double getAccountBalance(final Account account, final LocalDate date) {
        final LocalDate startDay = date.withDayOfMonth(1);
        final LocalDate endDay = startDay.plusMonths(1);
        final MonetaryAmount sum = this.databaseController
                .getTransactions(account, startDay, endDay)
                .stream()
                .map(transaction -> transaction.getAmount(account))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .reduce(
                        FastMoney.of(0, account.getCurrency().orElse(MoneyHelper.DEFAULT_CURRENCY)),
                        MonetaryAmount::add);
        return sum.getNumber().doubleValueExact();
    }

    public StringProperty getAnalysisNameProperty() {
        if (this.analysisName == null) {
            this.analysisName = new SimpleStringProperty();
        }
        return this.analysisName;
    }

    public ListProperty<PieChart.Data> getChartDataProperty() {
        if (this.chartData == null) {
            this.chartData = new SimpleListProperty<>();
        }
        return this.chartData;
    }

    public Property<LocalDate> getAnalysisDateProperty() {
        if (this.analysisDate == null) {
            this.analysisDate = new SimpleObjectProperty<>(LocalDate.now());
            this.analysisDate.addListener(event -> this.updateChartData());
        }
        return this.analysisDate;
    }
}
