package controller;

import model.Account;

import javax.inject.Inject;
import java.util.Collection;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 10/26/14.
 */
public class AccountController {

    private final Database database;

    @Inject
    public AccountController(final Database database) {
        this.database = database;
    }

    public Collection<Account> getAccounts() {
        return this.database.getAccounts();
    }
}
