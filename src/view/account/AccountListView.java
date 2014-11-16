package view.account;

import model.Account;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 10/20/14.
 */
public class AccountListView {

    public final Account account;

    public AccountListView(final Account account) {
        this.account = account;
    }

    public String getName() {
        return this.account.getName();
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
