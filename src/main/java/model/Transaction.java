package model;

import javax.money.MonetaryAmount;
import java.util.Optional;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 10/21/14.
 */
public class Transaction {

    private final Long id;
    private String name;
    private Account source;
    private Account sink;
    private MonetaryAmount amount;
    private Long date;

    public Transaction(final Long id, final Transaction transaction) {
        this(id, transaction.name, transaction.date, transaction.source, transaction.sink, transaction.amount);
    }

    public Transaction(
            final Long id,
            final String name,
            final Long date,
            final Account source,
            final Account sink,
            final MonetaryAmount amount) {
        this.id = id;
        this.name = name;
        this.source = source;
        this.sink = sink;
        this.amount = amount;
        this.date = date;
    }

    public Transaction(final MonetaryAmount amount, final Account source, final Account sink) {
        this();
        this.source = source;
        this.sink = sink;
        this.amount = amount;
    }

    public Transaction() {
        this.id = null;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public Optional<String> getName() {
        return Optional.ofNullable(this.name);
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Optional<MonetaryAmount> getAmount() {
        return Optional.ofNullable(amount);
    }

    public void setAmount(final MonetaryAmount amount) {
        this.amount = amount;
    }

    /**
     * Gets the amount in respect of the account. If the account is the sink of the transaction the amount is positive.
     * If the account is the source the amount will be negative.
     *
     * @param account The account to consider.
     * @return A positive amount if the account is the source, negative if it's the sink, empty otherwise.
     */
    public Optional<MonetaryAmount> getAmount(final Account account) {
        final Optional<MonetaryAmount> amount;
        if (this.getSink().isPresent() && this.getSink().get().equals(account)) {
            amount = this.getAmount();
        } else if (this.getSource().isPresent() && this.getSource().get().equals(account)) {
            amount = this.getAmount().map(MonetaryAmount::negate);
        } else {
            amount = Optional.empty();
        }
        return amount;
    }

    public Optional<Account> getSource() {
        return Optional.ofNullable(source);
    }

    public void setSource(final Account source) {
        this.source = source;
    }

    public Optional<Account> getSink() {
        return Optional.ofNullable(sink);
    }

    public void setSink(final Account sink) {
        this.sink = sink;
    }

    public Optional<Long> getDate() {
        return Optional.ofNullable(this.date);
    }

    public void setDate(final long date) {
        this.date = date;
    }

    public Optional<Long> getId() {
        return Optional.ofNullable(id);
    }
}
