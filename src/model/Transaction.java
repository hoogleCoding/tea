package model;

import javax.money.MonetaryAmount;
import java.util.Optional;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 10/21/14.
 */
public class Transaction {

    public final Optional<Long> id;
    public String name;
    private Account source;
    private Account sink;
    private MonetaryAmount amount;

    public Transaction(final Long id, final String name, final Account source, final Account sink, final MonetaryAmount amount) {
        this.id = Optional.ofNullable(id);
        this.name = name;
        this.source = source;
        this.sink = sink;
        this.amount = amount;
    }

    public Transaction(final MonetaryAmount amount, final Account source, final Account sink) {
        this();
        this.source = source;
        this.sink = sink;
        this.amount = amount;
    }

    public Transaction() {
        this.id = Optional.empty();
    }

    public MoneyTransaction of(final MonetaryAmount amount) {
        return new MoneyTransaction(amount);
    }

    class MoneyTransaction {
        private final MonetaryAmount amount;

        public MoneyTransaction(final MonetaryAmount amount) {
            this.amount = amount;
        }

        public SourcedTransaction from(final MoneyTransaction moneyTransaction, final Account source) {
            return new SourcedTransaction(moneyTransaction.amount, source);
        }

    }

    class SourcedTransaction {

        private final MonetaryAmount amount;
        private final Account source;

        public SourcedTransaction(final MonetaryAmount amount, final Account source) {
            this.amount = amount;
            this.source = source;
        }

        public Transaction to(final Account account) {
            return new Transaction(this.amount, this.source, account);
        }
    }

}
