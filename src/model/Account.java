package model;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 10/20/14.
 */
public class Account {
    private String name;

    public Account(final String name) {
        this.name = name;
    }

    public Account() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
