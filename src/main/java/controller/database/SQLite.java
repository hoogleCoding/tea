package controller.database;

import model.Account;
import model.AccountGroup;
import model.Transaction;
import org.javamoney.moneta.RoundedMoney;

import javax.money.MonetaryAmount;
import javax.money.MonetaryCurrencies;
import java.sql.*;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 10/26/14.
 */
public class SQLite implements Database {

    private Connection connection;
    private String databaseUrl;

    public SQLite() throws ClassNotFoundException {
        String sDriverName = "org.sqlite.JDBC";
        Class.forName(sDriverName);
        String databaseName = "fugger.db";
        String jdbc = "jdbc:sqlite";
        databaseUrl = String.format("%s:%s", jdbc, databaseName);
    }

    private Connection getConnection() {
        if (this.connection == null) {
            try {
                connection = DriverManager.getConnection(databaseUrl);
            } catch (SQLException e) {
                //TODO: use logger here.
                e.printStackTrace();
            }
        }
        this.setupDatabase();
        return this.connection;
    }

    private void closeConnection() {
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        this.connection = null;
    }

    private void setupDatabase() {
        try (final Statement statement = this.connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS account (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, creation_date INT NOT NULL, description TEXT, currency TEXT NOT NULL)");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS money_transaction (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, date INT NOT NULL, creation_date INT NOT NULL, monetary_amount TEXT NOT NULL, source INTEGER NOT NULL, sink INTEGER NOT NULL )");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS aggregate (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL)");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS account_aggregate (id INTEGER PRIMARY KEY AUTOINCREMENT, account INTEGER DEFAULT 0 NOT NULL, aggregate INTEGER DEFAULT 0 NOT NULL, FOREIGN KEY (account) REFERENCES account(id), FOREIGN KEY (aggregate) REFERENCES aggregate(id))");
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Account create(final Account account) {
        Account result = null;
        final String query = "INSERT INTO account (name, description, creation_date, currency) VALUES (?, ?, ?, ?)";
        try (final PreparedStatement statement = this.getConnection().prepareStatement(query)) {
            statement.setString(1, account.getName().get());
            statement.setString(2, account.getDescription().get());
            final Calendar calendar = Calendar.getInstance();
            final Timestamp timestamp = new Timestamp(calendar.getTime().getTime());
            statement.setTimestamp(3, timestamp);
            statement.setString(4, account.getCurrency().get().getCurrencyCode());
            statement.execute();
            final ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            result = new Account(resultSet.getLong(1), account.getName().get());
        } catch (SQLException e) {
            //TODO: log
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Account update(final Account account) {
        Account result = null;
        final String query = "UPDATE account SET name=?, description=?, currency=? WHERE id=?";
        try (final PreparedStatement statement = this.getConnection().prepareStatement(query)) {
            statement.setString(1, account.getName().get());
            statement.setString(2, account.getDescription().orElse(null));
            statement.setString(3, account.getCurrency().get().getCurrencyCode());
            statement.setLong(4, account.getId().get());
            statement.execute();
            result = account;
        } catch (SQLException e) {
            //TODO: log
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Collection<Account> getAccounts() {
        final Collection<Account> accounts = new LinkedList<>();
        try (final Statement statement = this.getConnection().createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM account");
            while (resultSet.next()) {
                final Long id = resultSet.getLong("id");
                final String accountName = resultSet.getString("name");
                final String description = resultSet.getString("description");
                final Account account = new Account(id, accountName);
                account.setDescription(description);
                account.setCreationTimestamp(resultSet.getLong("creation_date"));
                account.setCurrency(MonetaryCurrencies.getCurrency(resultSet.getString("currency")));
                accounts.add(account);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return accounts;
    }

    @Override
    public Optional<Account> getAccount(final Long id) {
        Optional<Account> result = Optional.empty();
        try (final Statement statement = this.getConnection().createStatement()) {
            final String query = String.format("SELECT * FROM account WHERE account.id = '%s'", id);
            final ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                final Long accountId = resultSet.getLong("id");
                final String accountName = resultSet.getString("name");
                final String description = resultSet.getString("description");
                final Account account = new Account(accountId, accountName);
                account.setDescription(description);
                account.setCreationTimestamp(resultSet.getLong("creation_date"));
                account.setCurrency(MonetaryCurrencies.getCurrency(resultSet.getString("currency")));
                result = Optional.of(account);
            }
        } catch (SQLException exception) {
            //TODO: log
            exception.printStackTrace();
        }
        return result;
    }

    @Override
    public Collection<Transaction> getTransactions() {
        final Collection<Transaction> transactions = new LinkedList<>();
        try (final Statement statement = this.getConnection().createStatement()) {
            final ResultSet result = statement.executeQuery("SELECT * FROM money_transaction");
            while (result.next()) {
                final Long id = result.getLong("id");
                final String name = result.getString("name");
                final Optional<Account> source = this.getAccount(result.getLong("source"));
                final Optional<Account> sink = this.getAccount(result.getLong("sink"));
                final MonetaryAmount money_amount = RoundedMoney.parse(result.getString("monetary_amount"));
                final Long date = result.getLong("date");
                if (source.isPresent() && sink.isPresent()) {
                    final Transaction transaction = new Transaction(id, name, date, source.get(), sink.get(), money_amount);
                    transactions.add(transaction);
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return transactions;
    }

    @Override
    public Collection<? extends Transaction> getTransactionsForAccount(final Long accountId) {
        final Collection<Transaction> transactions = new LinkedList<>();
        final String query = "SELECT * FROM money_transaction WHERE source = ? OR sink = ?";
        try (final PreparedStatement statement = this.getConnection().prepareStatement(query)) {
            statement.setLong(1, accountId);
            statement.setLong(2, accountId);
            final ResultSet result = statement.executeQuery();
            while (result.next()) {
                final Long id = result.getLong("id");
                final String name = result.getString("name");
                final Optional<Account> source = this.getAccount(result.getLong("source"));
                final Optional<Account> sink = this.getAccount(result.getLong("sink"));
                final MonetaryAmount money_amount = RoundedMoney.parse(result.getString("monetary_amount"));
                final Long date = result.getLong("date");
                if (source.isPresent() && sink.isPresent()) {
                    final Transaction transaction = new Transaction(id, name, date, source.get(), sink.get(), money_amount);
                    transactions.add(transaction);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    @Override
    public Transaction create(final Transaction transaction) {
        Transaction result = null;
        final String query = "INSERT INTO money_transaction (name, creation_date, \"date\", monetary_amount, source, sink)" +
                " VALUES (?, ?, ?, ?, ?, ?)";
        try (final PreparedStatement statement = this.getConnection().prepareStatement(query)) {
            statement.setString(1, transaction.getName().get());
            final Calendar calendar = Calendar.getInstance();
            final Timestamp timestamp = new Timestamp(calendar.getTime().getTime());
            statement.setTimestamp(2, timestamp);
            statement.setTimestamp(3, new Timestamp(transaction.getDate().get()));
            statement.setString(4, transaction.getAmount().get().toString());
            statement.setLong(5, transaction.getSource().get().getId().get());
            statement.setLong(6, transaction.getSink().get().getId().get());
            statement.executeUpdate();
            final ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            result = new Transaction(resultSet.getLong(1), transaction);
        } catch (SQLException e) {
            //TODO: log
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Transaction update(final Transaction transaction) {
        Transaction result = null;
        final String query = "UPDATE money_transaction SET name=?, date=?, monetary_amount=?, source=?, sink=? WHERE id=?";
        try (final PreparedStatement statement = this.getConnection().prepareStatement(query)) {
            statement.setString(1, transaction.getName().get());
            statement.setLong(2, transaction.getDate().get());
            statement.setString(3, transaction.getAmount().get().toString());
            statement.setLong(4, transaction.getSource().get().getId().get());
            statement.setLong(5, transaction.getSink().get().getId().get());
            statement.setLong(6, transaction.getId().get());
            statement.executeUpdate();
            result = transaction;
        } catch (SQLException e) {
            //TODO: log
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Optional<AccountGroup> create(final AccountGroup accountGroup) {
        AccountGroup addedGroup = null;
        final String query = "INSERT INTO aggregate (name) VALUES (?)";
        try (final PreparedStatement statement = this.getConnection().prepareStatement(query)) {
            statement.setString(1, accountGroup.getName().get());
            statement.executeUpdate();
            final ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            addedGroup = new AccountGroup(resultSet.getLong(1), accountGroup);
            final AccountGroup finalAddedGroup = addedGroup;
            accountGroup.getAccounts()
                        .stream()
                        .map(Account::getId)
                        .map(Optional::get)
                        .forEach(accountId -> this.saveAccountGroupToAccount(finalAddedGroup.getId().get(), accountId));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(addedGroup);
    }

    private void saveAccountGroupToAccount(final Long accountGroupId, final long accountId) {
        final String query = "INSERT INTO account_aggregate (account, aggregate) VALUES (?, ?)";
        try (final PreparedStatement statement = this.getConnection().prepareStatement(query)) {
            statement.setLong(1, accountId);
            statement.setLong(2, accountGroupId);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<AccountGroup> update(final AccountGroup accountGroup) {
        return Optional.empty();
    }

    @Override
    public Collection<AccountGroup> getAccountGroups() {
        final Collection<AccountGroup> groups = new LinkedList<>();
        final String query = "SELECT * FROM aggregate";
        try (final PreparedStatement statement = this.getConnection().prepareStatement(query)) {
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                final Long id = resultSet.getLong("id");
                final String name = resultSet.getString("name");
                groups.add(new AccountGroup(id, name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groups;
    }
}
