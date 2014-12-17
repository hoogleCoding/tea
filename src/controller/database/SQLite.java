package controller.database;

import model.Account;
import model.Transaction;
import org.javamoney.moneta.RoundedMoney;

import javax.money.MonetaryAmount;
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
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS account (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, creation_date INT NOT NULL)");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS money_transaction (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, date INT NOT NULL, creation_date INT NOT NULL, monetary_amount TEXT NOT NULL, source INTEGER NOT NULL, sink INTEGER NOT NULL )");
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Account save(final Account account) {
        Account result = null;
        try (final Statement statement = this.connection.createStatement()) {
            final String query;
            if (account.id.isPresent()) {
                query = String.format("UPDATE account SET name='%s', creation_date='%s' WHERE id=%s", account.getName(), account.getCreationTimestamp(), account.id.get());
            } else {
                query = String.format("INSERT INTO account (name, creation_date) VALUES ('%s', '%s')", account.getName(), account.getCreationTimestamp());
            }
            statement.executeUpdate(query);
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            result = new Account(resultSet.getLong(1), account.getName());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return result;
    }

    @Override
    public Collection<Account> getAccounts() {
        final Collection<Account> accounts = new LinkedList<>();
        try (final Statement statement = this.getConnection().createStatement()) {
            final ResultSet result = statement.executeQuery("SELECT * FROM account");
            while (result.next()) {
                final Long id = result.getLong("id");
                final String accountName = result.getString("name");
                final Account account = new Account(id, accountName);
                account.setCreationTimestamp(result.getLong("creation_date"));
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
                final Account account = new Account(accountId, accountName);
                account.setCreationTimestamp(resultSet.getLong("creation_date"));
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
            statement.setLong(5, transaction.getSource().get().id.get());
            statement.setLong(6, transaction.getSink().get().id.get());
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
        String query = "UPDATE money_transaction SET name=?, creation_date=?, date=?, monetary_amount=?, source=?, sink=? WHERE id=?";
        try (final PreparedStatement statement = this.getConnection().prepareStatement(query)) {
            statement.setString(1, transaction.getName().get());
        } catch (SQLException e) {
            //TODO: log
            e.printStackTrace();
        }
        return null;
    }
}
