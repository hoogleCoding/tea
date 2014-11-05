package controller;

import model.Account;
import model.Transaction;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 10/26/14.
 */
public interface Database {

    /**
     * Saves an Account in the database by either updating the account's record or creating a new database entry for the account.
     *
     * @param account The account to save or update
     * @return The saved account. If the account did not exist the returned account contains the primary key.
     */
    Account save(final Account account);

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
     * Returns all transactions from the database;
     *
     * @return A collection of all transactions found in the database;
     */
    Collection<Transaction> getTransactions();
}
