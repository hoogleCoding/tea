package model;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 10/20/14.
 */
public class Account {
    private final List<Position> positions;
    private String name;

    public Account(final String name) {
        this();
        this.name = name;
    }

    public Account() {
        this.positions = new LinkedList<>();
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
