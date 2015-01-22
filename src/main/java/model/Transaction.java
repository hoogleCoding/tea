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
