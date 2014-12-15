package controller.database;

import model.Account;
import model.Transaction;

import javax.inject.Inject;
import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Consumer;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 10/26/14.
 */
public class DatabaseController {

    private final Database database;
    private final Collection<Consumer<Account>> listeners;

    @Inject
    public DatabaseController(final Database database) {
        this.database = database;
        this.listeners = new LinkedList<>();
    }

    public Collection<Account> getAccounts() {
        return this.database.getAccounts();
    }

    public Account save(final Account account) {
        final Account saved = this.database.save(account);
        this.listeners.forEach(listener -> listener.accept(saved));
        return saved;
    }

    public void addChangeListener(final Consumer<Account> listener) {
        this.listeners.add(listener);
    }

    public Collection<Transaction> getTransactions() {
        return this.database.getTransactions();
    }

    /**
     * Saves a {@link model.Transaction} in the database if the {@link model.Transaction} does not exist it will be
     * created otherwise the existing data will be overwritten.
     *
     * @param transaction The transaction to save
     * @return The saved transaction with updated information from the database.
     */
    public Transaction save(final Transaction transaction) {
        return transaction
                .getId()
                .map(i -> this.database.update(transaction))
                .orElseGet(() -> this.database.create(transaction));
    }
}
