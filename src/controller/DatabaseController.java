package controller;

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
}
