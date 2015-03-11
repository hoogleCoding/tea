package model;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 2/10/15.
 */
public final class Analysis {

    private final String name;
    private final Long id;
    private final Collection<Account> accounts;

    public Analysis(final String name) {
        this.id = null;
        this.name = name;
        this.accounts = new LinkedList<>();
    }

    public Analysis(final long id, final Analysis analysis) {
        this.id = id;
        this.name = analysis.name;
        this.accounts = new LinkedList<>();
    }

    public Analysis(final Long id, final String name, final Collection<Account> accounts) {
        this.id = id;
        this.name = name;
        this.accounts = accounts;
    }

    public Optional<String> getName() {
        return Optional.ofNullable(this.name);
    }

    public Optional<Long> getId() {
        return Optional.ofNullable(this.id);
    }

    public void addAccount(final Account account) {
        this.accounts.add(account);
    }

    public Collection<Account> getAccounts() {
        return this.accounts;
    }
}
