package ViewModel.transaction;

import model.Transaction;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 1/14/15.
 */
public class TransactionListItemViewModel {

    private final Transaction transaction;

    public TransactionListItemViewModel(final Transaction transaction) {
        this.transaction = transaction;
    }

    @Override
    public String toString() {
        return this.transaction.getName().orElse("N/A");
    }
}
