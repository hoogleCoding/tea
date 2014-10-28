package controller;

import model.Account;

import java.sql.*;
import java.util.Collection;
import java.util.LinkedList;

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
                query = String.format("UPDATE account SET name='%s' WHERE id=%s", account.getName(), account.id.get());
            } else {
                query = String.format("INSERT INTO account (name, creation_date) VALUES ('%s', '0')", account.getName());
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
        final Collection<Account> accounts = new LinkedList<Account>();
        try (final Statement statement = this.getConnection().createStatement()) {
            final ResultSet result = statement.executeQuery("SELECT * FROM account");
            while (result.next()) {
                final Long id = result.getLong("id");
                final String accountName = result.getString("name");
                accounts.add(new Account(id, accountName));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return accounts;
    }
}
