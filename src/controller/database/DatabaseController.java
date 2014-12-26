package controller.database;

import model.Account;
import model.Transaction;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 10/26/14.
 */
@Singleton
public class DatabaseController {

    private final Database database;
    private final Collection<Consumer<Account>> accountListeners;
    private final Collection<Consumer<Transaction>> transactionListeners;

    @Inject
    public DatabaseController(final Database database) {
        this.database = database;
        this.accountListeners = new LinkedList<>();
        this.transactionListeners = new LinkedList<>();
    }

    public Collection<Account> getAccounts() {
        return this.database.getAccounts();
    }

    public Account save(final Account account) {
        final Account result = account
                .getId()
                .map(i -> this.database.update(account))
                .orElseGet(() -> this.database.create(account));
        this.accountListeners.forEach(listener -> listener.accept(result));
        return result;
    }

    public void addAccountChangeListener(final Consumer<Account> listener) {
        this.accountListeners.add(listener);
    }

    public void addTransactionChangeListener(final Consumer<Transaction> consumer) {
        this.transactionListeners.add(consumer);
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

}
