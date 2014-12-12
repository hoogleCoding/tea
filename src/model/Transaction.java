package model;

import javax.money.MonetaryAmount;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 10/21/14.
 */
public class Transaction {

    private final Optional<Long> id;
    private String name;
    private Account source;
    private Account sink;
    private MonetaryAmount amount;
    private LocalDate date;

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

    @Override
    public String toString() {
        return this.name;
    }

    public String getName() {
        return this.name;
    }

    public MonetaryAmount getAmount() {
        return amount;
    }

    public Account getSource() {
        return source;
    }

    public Account getSink() {
        return sink;
    }

    public LocalDate getDate() {
        return this.date;
    }
}
