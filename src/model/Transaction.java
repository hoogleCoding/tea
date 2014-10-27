package model;

import javax.money.MonetaryAmount;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 10/21/14.
 */
public class Transaction {

    public final String name;
    private final Account source;
    private final Account sink;
    private final MonetaryAmount amount;

    public Transaction(final String name, final Account source, final Account sink, final MonetaryAmount amount) {
        this.name = name;
        this.source = source;
        this.sink = sink;
        this.amount = amount;
    }
}
