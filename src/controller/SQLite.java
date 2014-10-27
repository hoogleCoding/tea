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
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS account (id NUMERIC PRIMARY KEY NOT NULL, name TEXT NOT NULL, creation_date INT NOT NULL)");
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Account save(Account account) {
        return null;
    }

    @Override
    public Collection<Account> getAccounts() {
        final Collection<Account> accounts = new LinkedList<>();
        try (final Statement statement = this.getConnection().createStatement()) {
            final ResultSet result = statement.executeQuery("SELECT * FROM account");
            while (result.next()) {
                final String accountName = result.getString("name");
                accounts.add(new Account(accountName));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return accounts;
    }
}
