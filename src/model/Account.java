package model;

import java.util.Optional;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 10/20/14.
 */
public class Account {
    public final Optional<Long> id;
    private String name;
    private Long creationTimestamp;

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

    public Long getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(final Long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (creationTimestamp != null ? !creationTimestamp.equals(account.creationTimestamp) : account.creationTimestamp != null)
            return false;
        if (!id.equals(account.id)) return false;
        return name.equals(account.name);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + (creationTimestamp != null ? creationTimestamp.hashCode() : 0);
        return result;
    }
}
