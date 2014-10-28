package model;

import java.util.Optional;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 10/20/14.
 */
public class Account {
    public final Optional<Long> id;
    private String name;

    public Account(final Long id, final String name) {
        this.id = Optional.of(id);
        this.name = name;
    }

    public Account() {
        this.id = Optional.empty();
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
