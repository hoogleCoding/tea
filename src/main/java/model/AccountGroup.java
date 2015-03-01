package model;

import view.account.AccountListView;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 2/10/15.
 */
public class AccountGroup {

    private final String name;
    private final Long id;
    private final Collection<Account> accounts;

    public AccountGroup(final String name) {
        this.id = null;
        this.name = name;
        this.accounts = new LinkedList<>();
    }

    public AccountGroup(final long id, final AccountGroup accountGroup) {
        this.id = id;
        this.name = accountGroup.name;
        this.accounts = new LinkedList<>();
    }

    public AccountGroup(final Long id, final String name) {
        this.id = id;
        this.name = name;
        this.accounts = new LinkedList<>();
    }

    public Optional<String> getName() {
        return Optional.ofNullable(this.name);
    }

    public Optional<Long> getId() {
        return Optional.ofNullable(this.id);
    }

    public void addAccount(final AccountListView accountListView) {
        this.accounts.add(accountListView.account);
    }

    public Collection<Account> getAccounts() {
        return this.accounts;
    }
}
