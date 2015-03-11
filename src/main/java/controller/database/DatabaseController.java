package controller.database;

import model.Account;
import model.Analysis;
import model.Transaction;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 10/26/14.
 */
@Singleton
public class DatabaseController {

    private final Database database;
    private final Collection<Consumer<Account>> accountListeners;
    private final Collection<Consumer<Transaction>> transactionListeners;
    private final Collection<Consumer<Analysis>> analysisListeners;

    @Inject
    public DatabaseController(final Database database) {
        this.database = database;
        this.accountListeners = new LinkedList<>();
        this.transactionListeners = new LinkedList<>();
        this.analysisListeners = new LinkedList<>();
    }

    public Collection<Account> getAccounts() {
        return this.database.getAccounts();
    }

    /**
     * Takes an {@link model.Account} and either creates it if it does not exist or updates the record in the database
     * if it is already existing.
     *
     * @param account The account to save to the database.
     * @return The account with the updated information or null if the record cold not be created or updated in the database.
     */
    public Account save(final Account account) {
        final Account result = account
                .getId()
                .map(i -> this.database.update(account))
                .orElseGet(() -> this.database.create(account));
        this.accountListeners.forEach(listener -> listener.accept(result));
        return result;
    }

    public void addAccountChangeListener(final Consumer<Account> consumer) {
        this.accountListeners.add(consumer);
    }

    public void addTransactionChangeListener(final Consumer<Transaction> consumer) {
        this.transactionListeners.add(consumer);
    }

    public void addAnalysisChangeListener(final Consumer<Analysis> consumer) {
        this.analysisListeners.add(consumer);
    }

    public Collection<Transaction> getTransactions() {
        return this.database.getTransactions();
    }

    /**
     * Gets all {@link model.Transaction}s associated with a given {@link model.Account}. The method will return an empty
     * collection if no transactions were found or the account cannot be found in the database.
     *
     * @param account The {@link model.Account} to get the transactions for
     * @return A collections of transactions for the given account.
     */
    public Collection<Transaction> getTransactions(final Account account) {
        final List<Transaction> result = new LinkedList<>();
        account.getId().ifPresent(id -> result.addAll(this.database.getTransactionsForAccount(id)));
        return result;
    }

    /**
     * Saves a {@link model.Transaction} in the database if the {@link model.Transaction} does not exist it will be
     * created otherwise the existing data will be overwritten.
     *
     * @param transaction The transaction to save
     * @return The saved transaction with updated information from the database.
     */
    public Transaction save(final Transaction transaction) {
        final Transaction result = transaction
                .getId()
                .map(i -> this.database.update(transaction))
                .orElseGet(() -> this.database.create(transaction));
        this.transactionListeners.forEach(listener -> listener.accept(result));
        return result;
    }

    public Collection<Analysis> getAccountGroups(){
        return this.database.getAnalysis();
    }

    /**
     * Saves a {@link model.Analysis} in the database. If the group does not exist it will be created. Otherwise the
     * existing database entry will be overwritten.
     *
     * @param analysis The account group to save
     * @return The saved account group with updated id.
     */
    public Optional<Analysis> save(final Analysis analysis) {
        final Optional<Analysis> addedGroup = analysis
                .getId()
                .map(i -> this.database.update(analysis))
                .orElseGet(() -> this.database.create(analysis));
        addedGroup.ifPresent(group -> this.analysisListeners.forEach(listener -> listener.accept(group)));
        return addedGroup;
    }
}
