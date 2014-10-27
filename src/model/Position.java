package model;

import javax.money.MonetaryAmount;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 10/20/14.
 */
public class Position {
    private String name;
    private MonetaryAmount amount;

    public Position(final String name, final MonetaryAmount amount) {
        this.name = name;
        this.amount = amount;
    }
}
