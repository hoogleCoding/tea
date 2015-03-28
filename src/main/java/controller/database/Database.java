package controller.database;

import model.Account;
import model.Analysis;
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
     * @return The saved transaction. With its ids set or Null if the account could not be created.
     */
    Account create(final Account account);

    /**
     * Updates a database record with the information from the {@link model.Account}.
     *
     * @param account The transaction to update in the database.
     * @return The updated transaction or Null if the account could not have been updated.
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
     * Returns all transactions between the startTimestamp and endTimestamp which are associated to an account.
     *
     * @param id             The id of the account
     * @param startTimestamp The start timestamp.
     * @param endTimeStamp   The end timestamp.
     * @return All transactions which matched the criteria or an empty collection if none were found.
     */
    Collection<? extends Transaction> getTransactionsForAccount(Long id, long startTimestamp, long endTimeStamp);

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

    /**
     * Creates a {@link model.Analysis} in the database.
     *
     * @param analysis The account group to create.
     * @return The saved account group with its id set.
     */
    Optional<Analysis> create(final Analysis analysis);

    /**
     * Updates a database record with the information from the {@link model.Analysis}.
     *
     * @param analysis The account group to update in the database.
     * @return The updated account group.
     */
    Optional<Analysis> update(final Analysis analysis);

    /**
     * Gets all AccountGroups from the database.
     *
     * @return A collection with all AccountGroups form the database.
     */
    Collection<Analysis> getAnalysis();

}
