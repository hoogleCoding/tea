package controller.database;

import model.Account;
import model.Transaction;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 10/26/14.
 */
public interface Database {

    /**
     * Creates a {@link model.Account} in the database.
     *
     * @param account The {@link model.Account} to create.
     * @return The saved transaction. With its ids set.
     */
    Account create(final Account account);

    /**
     * Updates a database record with the information from the {@link model.Account}.
     *
     * @param account The transaction to update in the database.
     * @return The updated transaction.
     */
    Account update(final Account account);

    /**
     * Returns all accounts from the database.
     *
     * @return A collection of all accounts found in the database.
     */
    Collection<Account> getAccounts();

    /**
     * Gets an account from the database based on its id.
     *
     * @param id The id of the account.
     * @return The account if found.
     */
    Optional<Account> getAccount(final Long id);

    /**
     * Returns all transactions from the database.
     *
     * @return A collection of all transactions found in the database.
     */
    Collection<Transaction> getTransactions();

    /**
     * Returns all transactions from the database which are associated to a given account's id.
     *
     * @return A collection of all transactions found in the database;
     */
    Collection<? extends Transaction> getTransactionsForAccount(final Long accountId);

    /**
     * Creates a {@link model.Transaction} in the database.
     *
     * @param transaction The {@link model.Transaction} to create.
     * @return The saved transaction. With its ids set.
     */
    Transaction create(final Transaction transaction);

    /**
     * Updates a database record with the information from the {@link model.Transaction}.
     *
     * @param transaction The transaction to update in the database.
     * @return The updated transaction.
     */
    Transaction update(final Transaction transaction);

}
